import { useEffect } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const socketUrl = 'http://localhost:8080/ws';

/**
 *
 * @param {'comments' | 'ratings' | 'scores'} topic
 * @param {string} gameName
 * @param {(data: any) => void} onMessage
 */
export default function useSocket(topic, gameName, onMessage) {
  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      onConnect: () => {
        client.subscribe(`/topic/${topic}/${gameName}`, (message) => {
          const data = JSON.parse(message.body);
          onMessage(data);
        });
      },
    });

    client.activate();
    return () => client.deactivate();
  }, [topic, gameName, onMessage]);
}
