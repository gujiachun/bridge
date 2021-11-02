package com.rainbow.bridge.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.rainbow.bridge.core.utils.MysqlJavaTypeMapping;
import com.rainbow.bridge.model.CanalModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author gujiachun
 */
public abstract class AbstractFlatMessageHandler implements MessageHandler<FlatMessage> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFlatMessageHandler.class);

    protected EntryHandler entryHandler;

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        logger.info("解析消息 {}", flatMessage);
        List<Map<String, String>> data = flatMessage.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                CanalEntry.EventType eventType = CanalEntry.EventType.valueOf(flatMessage.getType());
                List<Map<String, Object>> maps;
                if (eventType.equals(CanalEntry.EventType.UPDATE)) {
                    Map<String, Object> map = MysqlJavaTypeMapping.getObjectMap(flatMessage.getMysqlType(),data.get(i));
                    Map<String, Object> oldMap = MysqlJavaTypeMapping.getObjectMap(flatMessage.getMysqlType(),flatMessage.getOld().get(i));
                    maps = Stream.of(map, oldMap).collect(Collectors.toList());
                } else {
                    Map<String, Object> map = MysqlJavaTypeMapping.getObjectMap(flatMessage.getMysqlType(),data.get(i));
                    maps = Stream.of(map).collect(Collectors.toList());
                }
                try {
                    logger.info("消息处理器 {}", entryHandler);
                    if (entryHandler != null) {
                        CanalModel model = CanalModel.Builder.builder().id(flatMessage.getId()).table(flatMessage.getTable())
                                .executeTime(flatMessage.getEs()).database(flatMessage.getDatabase()).createTime(flatMessage.getTs()).build();
                        logger.info("消息发送至行处理 {} {}", maps, eventType);
                        handlerRowData(maps, entryHandler, eventType,model, flatMessage.getMysqlType());
                    }
                } catch (Exception e) {
                    logger.error("消息处理异常 将会做异常补偿 ，消息发送至行处理 {} {} {}", maps, eventType,e);
                    //to do记录消息异常的消息
                }
            }
        }
    }

    protected void handlerRowData(List<Map<String, Object>> list, EntryHandler entryHandler,
                                  CanalEntry.EventType eventType,CanalModel model,
                                  Map<String, String> mysqlTypeMap) throws Exception {
        if (entryHandler != null) {
            logger.info("处理消息 {}", list);
            switch (eventType) {
                case INSERT:
                    entryHandler.insert(model,mysqlTypeMap,list.get(0));
                    break;
                case UPDATE:
                    entryHandler.update(model,mysqlTypeMap,list.get(1), list.get(0));
                    break;
                case DELETE:
                    entryHandler.delete(model,mysqlTypeMap,list.get(0));
                    break;
                default:
                    logger.info("未知消息类型 {} 不处理 {}，model:{}", eventType, list,model);
                    break;
            }
        }
    }
}
