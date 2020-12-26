package guru.sfg.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Olivier Cappelle
 * @version x.x.x
 * @see
 * @since x.x.x 20/12/2020
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class BeerEvent implements Serializable {
    static final long serialVersionUID = -5_815_566_940_065_181_210L;

    private BeerDto beerDto;
}
