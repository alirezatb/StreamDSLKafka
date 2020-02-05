package alireza.ch4.transformer;

import alireza.model.Purchase;
import alireza.model.RewardAccumulator;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Objects;

public class TestPurchaseRewardTransformer implements ValueTransformer<Purchase, RewardAccumulator> {
    private final String storeName;
    private ProcessorContext processorContext;
    private KeyValueStore<String, Integer> stateStore;

    public TestPurchaseRewardTransformer(String storeName){
        Objects.requireNonNull(storeName, "Store Name cannot be null");
        this.storeName = storeName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.processorContext = processorContext;
        this.stateStore = (KeyValueStore) this.processorContext.getStateStore(storeName);
    }

    @Override
    public RewardAccumulator transform(Purchase purchase) {
        RewardAccumulator rewardAccumulator = RewardAccumulator.builder(purchase).build();
        Integer customerAccumulatedReward = this.stateStore.get(rewardAccumulator.getCustomerId());
        if (customerAccumulatedReward != null){
            rewardAccumulator.addRewardPoints(customerAccumulatedReward);
        }
        this.stateStore.put(rewardAccumulator.getCustomerId(), rewardAccumulator.getTotalRewardPoints());

        return rewardAccumulator;
    }

    @Override
    public void close() {

    }
}
