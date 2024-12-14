package org.zerock.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.*;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.stream.Collectors;

public class BoardSearchRepositoryImpl extends QuerydslRepositorySupport implements BoardSearchRepository {

    public BoardSearchRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(board.writer, member);
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        if (type != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (char t : type.toCharArray()) {
                switch (t) {
                    case 't': 
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case 'c':
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case 'w':
                        booleanBuilder.or(member.name.contains(keyword));
                        break;
                }
            }
            tuple.where(booleanBuilder);
        }

        tuple.groupBy(board);

        this.getQuerydsl().applyPagination(pageable, tuple);

        return new PageImpl<>(
            tuple.fetch().stream()
                .map(t -> new Object[]{t.get(board), t.get(member), t.get(reply.count())})
                .collect(Collectors.toList()),
            pageable,
            tuple.fetchCount()
        );
    }
} 