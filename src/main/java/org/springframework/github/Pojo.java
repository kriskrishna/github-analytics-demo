package org.springframework.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author Marcin Grzejszczak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pojo {
    @Getter
    @Setter
    @NotNull
    private String username;
    @Getter
    @Setter
    @NotNull
    private String repository;
    @Getter
    @Setter
    @NotNull
    private String type;
    @Getter
    @Setter
    @NotNull
    private String action;
}
