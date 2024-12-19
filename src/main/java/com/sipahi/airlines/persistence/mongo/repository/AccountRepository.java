package com.sipahi.airlines.persistence.mongo.repository;

import com.sipahi.airlines.enums.TestAccountType;
import com.sipahi.airlines.persistence.mongo.document.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<AccountDocument, String> {

    Optional<AccountDocument> findByAccountType(TestAccountType type);
}
