package com.team.hairdresser.utils.abstracts;

import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.persistence.criteria.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class BaseRepositoryCustomImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    private final Logger log = LoggerFactory.getLogger(BaseRepositoryCustomImpl.class);

    private JpaEntityInformation<T, ?> entityInformation;
    private EntityManager em;
    private static final String DELETED_FIELD = "deletedOn";

    public BaseRepositoryCustomImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.entityInformation = entityInformation;
        this.em = em;
    }

    @Override
    public void softDeleteAll() {
        for (T entity : findAll())
            deleteSoft(entity, Instant.now());
    }

    @Override
    public void softDelete(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        deleteSoft(id, Instant.now());
    }

    @Override
    public void softDelete(T entity) {
        Assert.notNull(entity, "The given entity must not be null!");
        deleteSoft(entity, Instant.now());
    }

    @Override
    public void softDelete(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        for (T entity : entities)
            deleteSoft(entity, Instant.now());
    }

    @Transactional
    @Override
    public List<T> findAll(Boolean baseStatus) {
        if (baseStatus) {
            return this.findAll();
        } else {
            return super.findAll();
        }
    }

    @Transactional
    @Override
    public List<T> findAll(Boolean baseStatus, Sort sort) {
        if (baseStatus) {
            return this.findAll(sort);
        } else {
            return super.findAll(sort);
        }
    }

    @Transactional
    @Override
    public Page<T> findAll(Boolean baseStatus, Pageable pageable) {
        if (baseStatus) {
            return this.findAll(pageable);
        } else {
            return super.findAll(pageable);
        }
    }

    @Transactional
    @Override
    public Page<T> findAll(Boolean baseStatus, Specification<T> spec, Pageable pageable) {
        if (baseStatus) {
            return this.findAll(spec, pageable);
        } else {
            return super.findAll(spec, pageable);
        }
    }

    @Override
    public List<T> findAll() {
        return super.findAll(notDeleted());
    }

    @Override
    public List<T> findAll(Sort sort) {
        return super.findAll(notDeleted(), sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return super.findAll(notDeleted(), pageable);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        if (spec == null) {
            return super.findAll(notDeleted(), pageable);
        }
        return super.findAll(spec.and(notDeleted()), pageable);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            return Collections.emptyList();
        }
        if (entityInformation.hasCompositeId()) {
            List<T> result = new ArrayList<>();
            for (ID id : ids) {
                result.add(getOne(id));
            }
            return result;
        }
        ByIdsSpecification<T> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<T> query = getQuery(Specification.where(specification).and(notDeleted()), (Sort) null);
        return query.setParameter(specification.parameter, ids).getResultList();
    }

    @Override
    public T getOne(ID id) {
        return super.findOne(Specification.where(new ByIdSpecification<T, ID>(entityInformation, id)).and(notDeleted())).orElse(null);
    }

    @Override
    public T getOne(Boolean baseStatus, ID id) {
        return baseStatus ? this.getOne(id) : this.findById(id).get();
    }

    @Override
    public Optional<T> findById(ID id) {
        return super.findOne(Specification.where(new ByIdSpecification<T, ID>(entityInformation, id)).and(notDeleted()));
    }

    @Override
    public Optional<T> findOne(Boolean baseStatus, Specification<T> spec) {
        if (baseStatus) {
            return this.findOne(spec);
        } else {
            return super.findOne(spec);
        }
    }

    @Override
    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        S result = this.save(entity);
        flush();
        return result;
    }


    @Override
    @Transactional
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();

        if (entities == null) {
            return result;
        }

        for (S entity : entities) {
            result.add(this.save(entity));
        }

        return result;

    }

    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        Set<ConstraintViolation<S>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator()
                .validate(entity);

        if (!constraintViolations.isEmpty())
            throw new ConstraintViolationException(constraintViolations.toString(), constraintViolations);

        Class<?> entityClass = entity.getClass();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<?> root = criteriaQuery.from(entityClass);

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (entityInformation.hasCompositeId()) {
            for (String s : entityInformation.getIdAttributeNames())
                predicates.add(criteriaBuilder.equal(root.<ID>get(s),
                        entityInformation.getCompositeIdAttributeValue(entityInformation.getId(entity), s)));

            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(DELETED_FIELD), Instant.now()));

            criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<Object> typedQuery = em.createQuery(criteriaQuery);
            List<Object> resultSet = typedQuery.getResultList();

            if (!resultSet.isEmpty()) {
                S result = (S) resultSet.get(0);
                BeanUtils.copyProperties(entity, result, getNullPropertyNames(entity));
                return super.save(result);
            }
        }

        if (entity.getClass().isAnnotationPresent(Table.class)) {
            Annotation a = entity.getClass().getAnnotation(Table.class);

            try {
                UniqueConstraint[] uniqueConstraints = (UniqueConstraint[]) a.annotationType()
                        .getMethod("uniqueConstraints").invoke(a);

                if (uniqueConstraints != null) {
                    for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
                        Map<String, Object> data = new HashMap<>();

                        for (String name : uniqueConstraint.columnNames()) {
                            if (name.endsWith("_id"))
                                name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
                                        name.substring(0, name.length() - 3));

                            PropertyDescriptor pd = new PropertyDescriptor(name, entityClass);
                            Object value = pd.getReadMethod().invoke(entity);

                            if (value == null) {
                                data.clear();
                                break;
                            }

                            data.put(name, value);
                        }

                        if (!data.isEmpty())
                            for (Map.Entry<String, Object> entry : data.entrySet())
                                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                    }

                    if (predicates.isEmpty())
                        return super.save(entity);

                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(DELETED_FIELD), Instant.now()));

                    criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
                    TypedQuery<Object> typedQuery = em.createQuery(criteriaQuery);
                    List<Object> resultSet = typedQuery.getResultList();

                    if (!resultSet.isEmpty()) {
                        S result = (S) resultSet.get(0);

                        BeanUtils.copyProperties(entity,
                                result, Stream
                                        .concat(Arrays.stream(getNullPropertyNames(entity)),
                                                Arrays.stream(
                                                        new String[]{entityInformation.getIdAttribute().getName()}))
                                        .toArray(String[]::new));

                        return super.save(result);
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException | IntrospectionException e) {
                log.error("Entity save etmeye çalışılırken baserepositoryde hata meydana geldi", NestedExceptionUtils.getMostSpecificCause(e));
            }
        }
        return super.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        T t = this.findById(id).orElseThrow(() -> {
            return new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!", this.entityInformation.getJavaType(), id), 1);
        });
        super.delete(t);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        super.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(Iterable<? extends T> entities) {
        super.deleteAll(entities);
    }

    @Override
    @Transactional
    public void deleteAll() {
        Iterator var1 = super.findAll().iterator();

        while (var1.hasNext()) {
            T element = (T) var1.next();
            super.delete(element);
        }
    }

    private void deleteSoft(ID id, Instant instant) {
        Assert.notNull(id, "The given id must not be null!");

        T entity = getOne(id);

        if (entity == null)
            throw new EmptyResultDataAccessException(
                    String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1);

        deleteSoft(entity, instant);
    }

    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, "id must not be null!");
        return getOne(id) != null ? true : false;
    }

    @Override
    public boolean existById(Boolean baseStatus, ID id) {
        if (baseStatus) {
            return this.existsById(id);
        } else {
            return super.existsById(id);
        }
    }

    @Override
    public List<T> findAllById(Boolean baseStatus, Iterable<ID> ids) {
        if (baseStatus) {
            return this.findAllById(ids);
        } else {
            return super.findAllById(ids);
        }
    }

    @SuppressWarnings("unchecked")
    private void deleteSoft(T entity, Instant instant) {
        Assert.notNull(entity, "The entity must not be null!");

        CriteriaBuilder cb = em.getCriteriaBuilder();

        Class<T> domainClass = (Class<T>) entity.getClass();

        CriteriaUpdate<T> update = cb.createCriteriaUpdate(domainClass);

        Root<T> root = update.from(domainClass);

        update.set(DELETED_FIELD, instant);

        final List<Predicate> predicates = new ArrayList<>();

        if (entityInformation.hasCompositeId()) {
            for (String s : entityInformation.getIdAttributeNames())
                predicates.add(cb.equal(root.<ID>get(s),
                        entityInformation.getCompositeIdAttributeValue(entityInformation.getId(entity), s)));
            update.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        } else
            update.where(cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()),
                    entityInformation.getId(entity)));

        em.createQuery(update).executeUpdate();
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> propertyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds)
            if (!pd.getName().equals(DELETED_FIELD) && src.getPropertyValue(pd.getName()) == null)
                propertyNames.add(pd.getName());

        return propertyNames.toArray(new String[propertyNames.size()]);
    }

    private static final class ByIdSpecification<T, ID extends Serializable> implements Specification<T> {

        private final JpaEntityInformation<T, ?> entityInformation;
        private final ID id;

        public ByIdSpecification(JpaEntityInformation<T, ?> entityInformation, ID id) {
            this.entityInformation = entityInformation;
            this.id = id;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            final List<Predicate> predicates = new ArrayList<>();
            if (entityInformation.hasCompositeId()) {
                for (String s : entityInformation.getIdAttributeNames())
                    predicates.add(cb.equal(root.<ID>get(s), entityInformation.getCompositeIdAttributeValue(id, s)));

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
            return cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()), id);
        }
    }

    @SuppressWarnings("rawtypes")
    private static final class ByIdsSpecification<T> implements Specification<T> {

        private final JpaEntityInformation<T, ?> entityInformation;

        ParameterExpression<Iterable> parameter;

        public ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Path<?> path = root.get(entityInformation.getIdAttribute());
            parameter = cb.parameter(Iterable.class);
            return path.in(parameter);
        }
    }

    private static final class DeletedIsNull<T> implements Specification<T> {

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            return criteriaBuilder.isNull(root.<Instant>get(DELETED_FIELD));
        }
    }

    private static final class DeletedTimeGreaterThanNow<T> implements Specification<T> {

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            return criteriaBuilder.greaterThan(root.<Instant>get(DELETED_FIELD), Instant.now());
        }
    }

    private static final <T> Specification<T> notDeleted() {
        return Specification.where(new DeletedIsNull<T>().or(new DeletedTimeGreaterThanNow<T>()));
    }

}

