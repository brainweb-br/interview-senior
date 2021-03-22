package br.com.brainweb.interview.core.features.hero;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class HeroComparisonResponseDTO implements Serializable {

    @ApiModelProperty(
            position = 1,
            value = "Herói mais forte baseado nos status")
    private HeroDTO strongestHero;

    @ApiModelProperty(
            position = 2,
            required = true,
            example = "100",
            value = "Diferença de força dos heróis")
    private Short strength;

    @ApiModelProperty(
            position = 3,
            required = true,
            example = "100",
            value = "Diferença de agilidade dos heróis")
    private Short agility;

    @ApiModelProperty(
            position = 4,
            required = true,
            example = "100",
            value = "Diferença de destreza dos heróis")
    private Short dexterity;

    @ApiModelProperty(
            position = 5,
            required = true,
            example = "100",
            value = "Diferença de inteligência dos heróis")
    private Short intelligence;
}
