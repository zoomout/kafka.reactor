package com.bz.kafka.reactor.persistence.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("messages")
@Data
@Builder
public class MessageEntity {

    @Id
    private UUID id;

    private String message;

}
