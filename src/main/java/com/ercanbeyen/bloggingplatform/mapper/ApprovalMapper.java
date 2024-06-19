package com.ercanbeyen.bloggingplatform.mapper;

import com.ercanbeyen.bloggingplatform.entity.Approval;
import com.ercanbeyen.bloggingplatform.entity.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ApprovalMapper {

    @Insert("""
            INSERT INTO APPROVALS (AUTHOR_ID, TICKET_ID)
            VALUES (#{authorId}, #{ticket.id})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertApproval(Approval approval);

    @Results(id = "approvalResultMap", value = {
            @Result(property = "id", column = "ID"),
            @Result(property = "authorId", column = "AUTHOR_ID"),
            @Result(property = "ticket",
                    column = "TICKET_ID",
                    javaType = Ticket.class,
                    one = @One(
                            select = "com.ercanbeyen.bloggingplatform.mapper.TicketMapper.findTicketById",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    @Select("""
            SELECT *
            FROM APPROVALS
            WHERE ID = #{id}
            """)
    Approval findApprovalById(@Param("id") Integer id);

    @ResultMap("approvalResultMap")
    @Select("""
            SELECT *
            FROM APPROVALS
            """)
    List<Approval> findAllApprovals();

    @Delete("""
            DELETE *
            FROM APPROVALS
            WHERE ID = #{id}
            """)
    void deleteApprovalById(Integer id);

    @ResultMap("approvalResultMap")
    @Select("""
            SELECT *
            FROM APPROVALS
            WHERE TICKET_ID = #{ticketId}
            """)
    List<Approval> findAllApprovalsByTicketId(@Param("ticketId") Integer ticketId);
}
