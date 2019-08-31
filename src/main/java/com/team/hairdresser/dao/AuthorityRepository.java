package com.team.hairdresser.dao;


import com.team.hairdresser.domain.Authority;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AuthorityRepository extends BaseRepository<Authority, Long> {

    List<Authority> findByParentAuthorityOrderById(Authority parentAuthority);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from Authority where (ParentId is not null)")
    void deleteFirst();

    Authority findByTitle(String title);
}
