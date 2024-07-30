package umc.SukBakJi.domain.model.entity.mapping;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BoardLikeId implements java.io.Serializable {
    private Long memberId;
    private Long boardId;
}
