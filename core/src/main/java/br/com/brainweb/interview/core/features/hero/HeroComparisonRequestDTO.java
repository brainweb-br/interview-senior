package br.com.brainweb.interview.core.features.hero;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class HeroComparisonRequestDTO implements Serializable {

    @ApiModelProperty(
            position = 1,
            value = "Herói número 1")
    private HeroDTO hero1;

    @ApiModelProperty(
            position = 2,
            value = "Herói número 2")
    private HeroDTO hero2;
}
