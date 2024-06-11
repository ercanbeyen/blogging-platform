package com.ercanbeyen.bloggingplatform.mapper;

import com.ercanbeyen.bloggingplatform.entity.Ticket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TicketMapper {
    @Insert("""
            INSERT INTO tickets(description)
            VALUES (#{description})
            """)
    void insertTicket(Ticket ticket);

    @Update("""
            UPDATE tickets
            SET description = #{ticket.description}
            WHERE id = #{id}
            """)
    void updateTicket(@Param("id") Integer id, @Param("ticket") Ticket ticket);

    @Select("""
            SELECT *
            FROM tickets
            WHERE id = #{id}
            """)
    Ticket findTicket(@Param("id") Integer id);

    @Select("""
            SELECT *
            FROM tickets
            """)
    List<Ticket> findTickets();

    @Delete("""
            DELETE
            FROM tickets
            WHERE id = #{id}
            """)
    void deleteTicket(@Param("id") Integer id);
}
