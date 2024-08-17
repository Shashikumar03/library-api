package org.example.library.repositories;

import org.example.library.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    //    List<Message> findBySenderIdAndReceiverIdOrderByTimestampAsc(Long senderId, Long receiverId);
//        List<Message> findBySenderIdOrReceiverIdOrderByTimestampAsc(Long userId1, Long userId2);
    @Query("SELECT m FROM Message m WHERE (m.senderId = :senderId AND m.receiverId = :receiverId) OR (m.senderId = :receiverId AND m.receiverId = :senderId) ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(@Param("senderId") String senderId, @Param("receiverId") String receiverId);


}