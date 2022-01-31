package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
@Scope("prototype")
public class Currency {

    private String name;
    private String code;
    private String org;
}
