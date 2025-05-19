package com.example.mapper;

import com.example.model.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketMapper {
    @Select("SELECT " +
            "ID, " +
            "TITLE, " +
            "CREATED_USER AS createdUsersUsername, " +
            "SUBSTRING_INDEX(" +
            "    SUBSTRING_INDEX(" +
            "        JSON_UNQUOTE(JSON_EXTRACT(DETAIL, '$.individual[5].editableAttachmentBlock')), " +
            "        '<dd class=\"attachment-author\">', " +
            "        -1" +
            "    ), " +
            "    '</dd>', " +
            "    1" +
            ") AS createdUser, " +
            "CREATED_TIME AS createTime, " +
            "MODIFIED_TIME AS finishedTime, " +
            "LOCATION_ID AS locationId, " +
            "STATUS, " +
            "CONCAT(" +
            "    JSON_UNQUOTE(JSON_EXTRACT(DETAIL, '$.individual[0].value')), " +
            "    ' - ', " +
            "    JSON_UNQUOTE(JSON_EXTRACT(DETAIL, '$.individual[0].text'))" +
            ") AS approver, " +
            "JSON_UNQUOTE(JSON_EXTRACT(DETAIL, '$.individual[1].value')) AS requestDescription, " +
            "JSON_UNQUOTE(JSON_EXTRACT(DETAIL, '$.individual[2].value')) AS benefitReason, " +
            "JSON_UNQUOTE(JSON_EXTRACT(DETAIL, '$.individual[3].value')) AS impactedApplications " +
            "FROM tickets " +
            "ORDER BY ID DESC")
    List<Ticket> findAllTickets();
}