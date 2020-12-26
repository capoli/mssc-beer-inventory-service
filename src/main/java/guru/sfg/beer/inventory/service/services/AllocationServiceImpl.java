package guru.sfg.beer.inventory.service.services;

import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.events.BeerOrderDto;
import guru.sfg.brewery.model.events.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

/**
 * @author Olivier Cappelle
 * @version x.x.x
 * @see
 * @since x.x.x 26/12/2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {
    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrder) {
        log.debug("Allocating OrderId: {}", beerOrder.getId());

        var totalOrder = new AtomicInteger();
        var totalAllocated = new AtomicInteger();

        beerOrder.getBeerOrderLines().forEach(beerOrderLine -> {
            if (getQuantity(beerOrderLine.getQuantityAllocated()) - getQuantity(beerOrderLine.getQuantityAllocated()) > 0) {
                allocateOrderLine(beerOrderLine);
            }
            totalOrder.set(totalOrder.get() + getQuantity(beerOrderLine.getQuantityAllocated()));
            totalAllocated.set(totalAllocated.get() + getQuantity(beerOrderLine.getQuantityAllocated()));
        });
        log.debug("Total ordered: {} Total allocated: {}", totalOrder.get(), totalAllocated.get());

        return totalOrder.get() == totalAllocated.get();
    }

    private void allocateOrderLine(BeerOrderLineDto beerOrderLine) {
        beerInventoryRepository.findAllByUpc(beerOrderLine.getUpc()).forEach(beerInventory -> {
            int inventory = getQuantity(beerInventory.getQuantityOnHand());
            int orderQty = getQuantity(beerOrderLine.getQuantityAllocated());
            int allocatedQty = getQuantity(beerOrderLine.getQuantityAllocated());
            int qtyToAllocate = orderQty - allocatedQty;

            if (inventory >= qtyToAllocate) { //full allocation
                inventory = inventory - qtyToAllocate;
                beerOrderLine.setQuantityAllocated(orderQty);
                beerInventory.setQuantityOnHand(inventory);

                beerInventoryRepository.save(beerInventory);
            } else if (inventory > 0) { //partial allocation
                beerOrderLine.setQuantityAllocated(allocatedQty + inventory);
                beerInventory.setQuantityOnHand(0);

                beerInventoryRepository.delete(beerInventory);
            }
        });

    }

    private int getQuantity(Integer quantity) {
        return nonNull(quantity) ? quantity : 0;
    }
}
