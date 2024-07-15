package com.ercanbeyen.bloggingplatform.mapper;

import com.ercanbeyen.bloggingplatform.entity.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TicketMapper {

    @Insert("""
            INSERT INTO TICKETS (DESCRIPTION, STATUS, CREATED_AT, UPDATED_AT)
            VALUES (#{description}, 'TO_DO', LOCALTIMESTAMP(), LOCALTIMESTAMP())
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
            @Result(property = "status", column = "STATUS"),
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
                SELECT tickets.ID, tickets.DESCRIPTION, tickets.CREATED_AT, tickets.UPDATED_AT
                FROM (
                    SELECT COUNT(*) AS NUMBER_OF_APPROVALS, tickets1.ID, tickets1.DESCRIPTION, tickets1.CREATED_AT, tickets1.UPDATED_AT
                    FROM TICKETS tickets1
                    INNER JOIN APPROVALS approvals ON tickets1.ID = approvals.TICKET_ID
                    GROUP BY approvals.TICKET_ID
                    <if test = "minimumApprovals != null">
                        HAVING NUMBER_OF_APPROVALS >= #{minimumApprovals}
                    </if>
                    <if test = "sortBy != null and order != null">
                        ORDER BY
                        <choose>
                            <when test = "sortBy == 'createdAt'">
                                CREATED_AT
                            </when>
                            <when test = "sortBy == 'updatedAt'">
                                UPDATED_AT
                            </when>
                        </choose>
                        <choose>
                            <when test = "order == 'desc'">
                                DESC
                            </when>
                            <when test = "order == 'asc'">
                                ASC
                            </when>
                        </choose>
                    </if>
                    <if test = "topApproved != null">
                        LIMIT #{topApproved}
                    </if>
                ) AS tickets
                <where>
                    <if test = "createdYear != null">
                        tickets.YEAR(CREATED_AT) = #{createdYear}
                    </if>
                    <if test = "updatedYear != null">
                        AND tickets.YEAR(UPDATED_AT) = #{updatedYear}
                    </if>
                </where>
            </script>
            """)
    List<Ticket> findAllTickets(
            @Param("createdYear") Integer createdYear,
            @Param("updatedYear") Integer updatedYear,
            @Param("minimumApprovals") Integer minimumNumberOfApprovalsForTicket,
            @Param("sortBy") String sortedField,
            @Param("order") String order,
            @Param("topApproved") Integer numberOfTopApprovedTickets
    );

    @Delete("""
            DELETE
            FROM TICKETS
            WHERE ID = #{id}
            """)
    void deleteTicketById(@Param("id") Integer id);
}
