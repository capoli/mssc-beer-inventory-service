package guru.sfg.beer.inventory.service.services;

import guru.sfg.brewery.model.events.BeerOrderDto;

/**
 * @author Olivier Cappelle
 * @version x.x.x
 * @see
 * @since x.x.x 26/12/2020
 **/
public interface AllocationService {
    Boolean allocateOrder(BeerOrderDto beerOrder);
}
