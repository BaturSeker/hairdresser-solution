package com.team.hairdresser.utils.util;


import com.team.hairdresser.dto.combo.ComboResponseDto;

import java.util.ArrayList;
import java.util.List;

public final class ComboResponseBuilder {

    private ComboResponseBuilder() {
    }

    public static List<ComboResponseDto> buildComboResponseList(List<Object[]> resultList) {
        List<ComboResponseDto> comboResultList = new ArrayList<>();

        for (Object[] resultItem : resultList) {
            ComboResponseDto comboResponseDto = new ComboResponseDto();
            comboResponseDto.setValue(String.valueOf(resultItem[0]));
            comboResponseDto.setText(String.valueOf(resultItem[1]));
            comboResultList.add(comboResponseDto);
        }

        return comboResultList;
    }

    public static List<ComboResponseDto> buildUniqueItemedComboResponseList(List<Object[]> resultList) {
        List<ComboResponseDto> comboResultList = new ArrayList<>();

        for (Object[] resultItem : resultList) {
            ComboResponseDto comboResponseDto = new ComboResponseDto();
            comboResponseDto.setValue(String.valueOf(resultItem[0]));
            comboResponseDto.setText(String.valueOf(resultItem[1]));
            if (!comboResultList.contains(comboResponseDto)) {
                comboResultList.add(comboResponseDto);
            }
        }

        return comboResultList;
    }

    public static List<ComboResponseDto> buildEnumComboResponseList(Enum[] resultList) {
        List<ComboResponseDto> comboResultList = new ArrayList<>();

        for (Enum resultItem : resultList) {
            ComboResponseDto comboResponseDto = new ComboResponseDto();
            comboResponseDto.setValue(String.valueOf(resultItem.ordinal() + 1));
            comboResponseDto.setText(String.valueOf(resultItem.toString()));
            comboResultList.add(comboResponseDto);
        }

        return comboResultList;
    }
}
