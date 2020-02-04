package io.github.pashazz.taskmanager.trigger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class UnassignTaskOnRemovePersonFromProject implements Trigger {
    private static Log LOG = LogFactory.getLog(UnassignTaskOnRemovePersonFromProject.class);
    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        LOG.debug("Initializing trigger");
    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        Long person = (Long)oldRow[0];
        Long project = (Long)oldRow[1];
        var st = connection.prepareStatement("update task set person_fk=null where person_fk=? and project_fk=?");
        st.setLong(1, person);
        st.setLong(2, project);
        var good = st.executeUpdate();
        LOG.debug(st + "; updated: " + good);



    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void remove() throws SQLException {

    }
}
