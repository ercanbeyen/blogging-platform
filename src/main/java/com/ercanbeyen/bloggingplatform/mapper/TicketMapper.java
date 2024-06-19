package com.ercanbeyen.bloggingplatform.mapper;

import com.ercanbeyen.bloggingplatform.entity.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TicketMapper {

    @Insert("""
            INSERT INTO TICKETS (DESCRIPTION, CREATED_AT, UPDATED_AT)
            VALUES (#{description}, LOCALTIMESTAMP(), LOCALTIMESTAMP())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTicket(Ticket ticket);

    @Update("""
            <script>
                UPDATE TICKETS
                <set>
                    <if test = "ticket.description != null">
                        DESCRIPTION = #{ticket.description}
                    </if>,
                    UPDATED_AT = LOCALTIMESTAMP()
                </set>
                WHERE ID = #{id}
            </script>
            """)
    void updateTicket(@Param("id") Integer id, @Param("ticket") Ticket ticket);

    @Results(id = "ticketResultMap", value = {
            @Result(property = "id", column = "ID"),
            @Result(property = "description", column = "DESCRIPTION"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "updatedAt", column = "UPDATED_AT"),
            @Result(property = "approvals",
                    column = "ID",
                    javaType = List.class,
                    many = @Many(
                            select = "com.ercanbeyen.bloggingplatform.mapper.ApprovalMapper.findAllApprovalsByTicketId",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    @Select("""
            SELECT *
            FROM TICKETS
            WHERE ID = #{id}
            """)
    Ticket findTicketById(@Param("id") Integer id);

    @ResultMap("ticketResultMap")
    @Select("""
            <script>
                SELECT *
                FROM TICKETS
                <where>
                    <if test = "createdYear != null">
                        YEAR(CREATED_AT) = #{createdYear}
                    </if>
                    <if test = "updatedYear != null">
                        AND YEAR(UPDATED_AT) = #{updatedYear}
                    </if>
                </where>
            </script>
            """)
    List<Ticket> findAllTickets(@Param("createdYear") Integer createdYear, @Param("updatedYear") Integer updatedYear);

    @Delete("""
            DELETE
            FROM TICKETS
            WHERE ID = #{id}
            """)
    void deleteTicketById(@Param("id") Integer id);
}
