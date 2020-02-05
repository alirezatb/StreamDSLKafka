package alireza.ch4.transformer;

import alireza.model.Purchase;
import alireza.model.RewardAccumulator;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Objects;

public class PurchaseRewardTransformer implements ValueTransformer<Purchase, RewardAccumulator> {

    private KeyValueStore<String, Integer> storeState;
    private final String storeName;
    private ProcessorContext processorContext;

    public PurchaseRewardTransformer(String storeName) {
        Objects.requireNonNull(storeName, "Object StoreName cannot be null");
        this.storeName = storeName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.processorContext = processorContext;
        this.storeState = (KeyValueStore) this.processorContext.getStateStore(storeName);
    }

    @Override
    public RewardAccumulator transform(Purchase purchase) {
        RewardAccumulator rewardAccumulator = RewardAccumulator.builder(purchase).build();
        Integer currentAccumulatedReward = this.storeState.get(rewardAccumulator.getCustomerId());
        if (currentAccumulatedReward !=null){
            rewardAccumulator.addRewardPoints(currentAccumulatedReward);
        }
        this.storeState.put(rewardAccumulator.getCustomerId(), rewardAccumulator.getTotalRewardPoints());
        return rewardAccumulator;
    }

    @Override
    public void close() {

    }
}
