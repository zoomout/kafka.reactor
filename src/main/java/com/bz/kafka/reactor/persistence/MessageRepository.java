package com.bz.kafka.reactor.persistence;

import com.bz.kafka.reactor.persistence.dao.MessageEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Messages repository
 */
public interface MessageRepository extends ReactiveCrudRepository<MessageEntity, String> {
}
