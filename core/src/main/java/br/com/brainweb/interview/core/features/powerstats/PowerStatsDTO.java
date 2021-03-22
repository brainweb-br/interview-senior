package br.com.brainweb.interview.core.features.powerstats;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PowerStatsDTO implements Serializable {

    @ApiModelProperty(
            position = 1,
            example = "6b3c84e6-bf4d-4bb0-b4a4-db1ed06b2a80",
            value = "Id do atributo")
    private UUID id;

    @ApiModelProperty(
            position = 2,
            required = true,
            example = "100",
            value = "Força do herói")
    @NotNull(message = "Campo obrigatório: strength")
    private Short strength;

    @ApiModelProperty(
            position = 3,
            required = true,
            example = "100",
            value = "Agilidade do herói")
    @NotNull(message = "Campo obrigatório: agility")
    private Short agility;

    @ApiModelProperty(
            position = 4,
            required = true,
            example = "100",
            value = "Destreza do herói")
    @NotNull(message = "Campo obrigatório: dexterity")
    private Short dexterity;

    @ApiModelProperty(
            position = 5,
            required = true,
            example = "100",
            value = "Inteligência do herói")
    @NotNull(message = "Campo obrigatório: intelligence")
    private Short intelligence;

    @ApiModelProperty(
            position = 6,
            example = "2021-03-20T23:02:24.384Z",
            value = "Data de criação")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("createdAt")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @ApiModelProperty(
            position = 7,
            example = "2021-03-20T23:02:24.384Z",
            value = "Data de última atualização")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("updatedAt")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;
}
