package com.sourcery.defect_registration_system.config.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UuidTypeHandlerTest {

    UuidTypeHandler handler = new UuidTypeHandler();

    @Test
    void setNonNullParameter_setsObject() throws Exception {
        PreparedStatement ps = Mockito.mock(PreparedStatement.class);
        UUID id = UUID.randomUUID();

        handler.setNonNullParameter(ps, 1, id, JdbcType.OTHER);

        Mockito.verify(ps).setObject(1, id);
    }

    @Test
    void getNullableResult_byName_returnsUuid() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        UUID id = UUID.randomUUID();
        Mockito.when(rs.getString("id")).thenReturn(id.toString());

        UUID actual = handler.getNullableResult(rs, "id");

        assertEquals(id, actual);
    }

    @Test
    void getNullableResult_byIndex_nullIsNull() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getString(2)).thenReturn(null);

        UUID actual = handler.getNullableResult(rs, 2);

        assertNull(actual);
    }

    @Test
    void getNullableResult_callable_badUuid_throws() throws Exception {
        CallableStatement cs = Mockito.mock(CallableStatement.class);
        Mockito.when(cs.getString(1)).thenReturn("not-a-uuid");

        assertThrows(IllegalArgumentException.class, () ->
                handler.getNullableResult(cs, 1)
        );
    }
}
