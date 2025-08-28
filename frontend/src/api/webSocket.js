import { useEffect } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const resolveSocketUrl = () => {
  const base = import.meta.env?.VITE_BASE_URL?.trim();
  if (base) return `${base.replace(/\/+$/, "")}/ws`; // dev: http://localhost:8080/ws
  return "/ws"; // prod/ngrok: відносний URL -> https://<твій-домен>/ws
};

/**
 * @param {'comments'|'ratings'|'scores'} topic
 * @param {string} gameName
 * @param {(data:any)=>void} onMessage
 */
export default function useSocket(topic, gameName, onMessage) {
  useEffect(() => {
    const socketUrl = resolveSocketUrl();

    const client = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      reconnectDelay: 5000,
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
