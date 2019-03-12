package quick.pager.id.generator.web.dao;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import quick.pager.id.generator.web.model.Segment;

public class GeneratorDao {


    // 查询所有的自增策略sql语句
    private static final String ALL_SQL = "select `id`, `biz_name`, `description`, `segment`,  `biz_type`, `steps`, `create_time`, `delete_status` from `segment` where `delete_status` = 0";
    // 根据biz_name 查询Id的自增策略
    private static final String SELECT_ONE_SQL = "select `id`, `biz_name`, `description`, `segment`,  `biz_type`, `steps`, `create_time`, `delete_status` from `segment` where `biz_name` = ? and `delete_status` = 0";
    // 根据biz_name 更新segment 值
    private static final String UPDATE_LOAD_SEGMENT_SQL = "update `segment` set `segment`= `segment` + `steps` where `biz_name` = ?";


    private JdbcTemplate jdbcTemplate;

    public GeneratorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询所有业务号段
     */
    public List<Segment> selectAll() {
        return jdbcTemplate.query(ALL_SQL, new BeanPropertyRowMapper<>(Segment.class));
    }


    public Segment updateAndLoadSegment(String bizName) {

        int update = jdbcTemplate.update(UPDATE_LOAD_SEGMENT_SQL, bizName);
        if (update < 0) {
            throw new RuntimeException("号段值更新异常 bizName = " + bizName);
        }

        return jdbcTemplate.queryForObject(SELECT_ONE_SQL, new Object[]{bizName}, new BeanPropertyRowMapper<>(Segment.class));
    }
}
