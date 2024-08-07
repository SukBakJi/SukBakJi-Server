package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.mapping.FavoriteLab;

public class LabConverter {
    public static FavoriteLab toFavoriteLab(Member member, Lab lab){
        return FavoriteLab.builder()
                .member(member)
                .lab(lab)
                .build();
    }
}
