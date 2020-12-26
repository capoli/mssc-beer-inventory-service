package guru.sfg.common.events;

import lombok.NoArgsConstructor;

/**
 * @author Olivier Cappelle
 * @version x.x.x
 * @see
 * @since x.x.x 20/12/2020
 **/
@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {
    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
