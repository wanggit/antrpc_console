package io.github.wanggit.antrpc.console.zookeeper;

import io.github.wanggit.antrpc.console.web.vo.SubscribeNodeVO;

import java.util.List;

public interface ISubscribeContainer {

    void addSubscribeNode(SubscribeNode subscribeNode);

    void deleteSubscribeNode(SubscribeNode subscribeNode);

    List<SubscribeNodeVO> findByClassName(String className);
}
