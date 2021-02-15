package org.tenpo.test.mstenpotest.multiply;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.tenpo.test.mstenpotest.KafkaMessageProducer;
import org.tenpo.test.mstenpotest.exceptions.InvalidInputException;

import java.util.Optional;

@Component
@Slf4j
public class MultiplyServiceImpl implements MultiplyService {

    private final MultiplyRepository multiplyRepository;

    private final KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    public MultiplyServiceImpl(MultiplyRepository multiplyRepository, KafkaMessageProducer kafkaMessageProducer) {
        this.multiplyRepository = multiplyRepository;
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    @Override
    public ResultResponse multiply(MultiplyRequest multiplyRequest) {
        Optional<ResultResponse> resultOptional = getResult(multiplyRequest);
        ResultResponse resultResponse = resultOptional.orElseThrow(() -> {
            log.debug("The multiply operation can't be aply to {}", multiplyRequest);
            return new InvalidInputException("The multiply operation can't be aply");
        });
        kafkaMessageProducer.sendMessage(
                buildMultiplyEntity(multiplyRequest, resultResponse)
        );
        return resultResponse;
    }

    private MultiplyEntity buildMultiplyEntity(MultiplyRequest multiplyRequest, ResultResponse resultResponse) {
        return new MultiplyEntity(multiplyRequest.getNumberA(), multiplyRequest.getNumberB(), resultResponse.getResult());
    }

    @Override
    public void save(MultiplyEntity multiplyEntity) {
        multiplyRepository.save(multiplyEntity);
    }

    @Override
    public Page<MultiplyResponse> getMultiplyHistoryPages(Pageable Pageable) {
        Page<MultiplyEntity> multiplyEntities = multiplyRepository.findAll(Pageable);
        Page<MultiplyResponse> multiplyResponses = multiplyEntities.map(
                multiplyEntity -> new MultiplyResponse(
                        multiplyEntity.getNumberA(),
                        multiplyEntity.getNumberB(),
                        multiplyEntity.getResult()
                )
        );
        return multiplyResponses;
    }

    private Optional<ResultResponse> getResult(MultiplyRequest multiplyRequest) {
        if (multiplyRequest.getNumberA() == null || multiplyRequest.getNumberB() == null)
            return Optional.empty();
        return Optional.of(new ResultResponse(multiplyRequest.getNumberA().multiply(multiplyRequest.getNumberB())));
    }
}
