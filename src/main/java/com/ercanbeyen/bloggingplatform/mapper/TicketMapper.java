package com.ercanbeyen.bloggingplatform.mapper;

import com.ercanbeyen.bloggingplatform.entity.Ticket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TicketMapper {

    @Insert("""
            INSERT INTO TICKETS (DESCRIPTION)
            VALUES (#{description})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTicket(Ticket ticket);

    @Update("""
            UPDATE TICKETS
            SET DESCRIPTION = #{ticket.description}
            WHERE ID = #{id}
            """)
    void updateTicket(@Param("id") Integer id, @Param("ticket") Ticket ticket);

    @Results(id = "ticketResultMap", value = {
            @Result(property = "id", column = "ID"),
            @Result(property = "description", column = "DESCRIPTION")
    })
    @Select("""
            SELECT *
            FROM TICKETS
            WHERE ID = #{id}
            """)
    Ticket findTicket(@Param("id") Integer id);

    @ResultMap("ticketResultMap")
    @Select("""
            SELECT *
            FROM TICKETS
            """)
    List<Ticket> findTickets();

    @Delete("""
            DELETE
            FROM TICKETS
            WHERE ID = #{id}
            """)
    void deleteTicket(@Param("id") Integer id);
}
