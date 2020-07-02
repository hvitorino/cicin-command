(ns cicin-command.core
  (:require
    [monger.core :as mg]
    [monger.collection :as mg.coll])
  (:gen-class)
  (:import (org.apache.kafka.clients.consumer
             KafkaConsumer
             ConsumerRecords
             ConsumerRecord)))

(defn open-conn []
  (mg/connect-via-uri "mongodb://127.0.0.1:27017/messages-db"))

(defn close-conn [mongo]
  (mg/disconnect (:conn mongo)))

(def cfg {"bootstrap.servers"  "localhost:9092"
          "group.id"           "cicin-command"
          "enable.auto.commit" "true"
          "max.poll.records"   "1"
          "value.deserializer" "org.apache.kafka.common.serialization.StringDeserializer"
          "key.deserializer"   "org.apache.kafka.common.serialization.StringDeserializer"})

(defn consume [topics]
  (let [consumer (KafkaConsumer. cfg)]
    (.subscribe consumer topics)
    consumer))

(def should-run (atom false))

(defn play [^KafkaConsumer consumer {:keys [db]}]
  (swap! should-run not)
  (loop []
    (when (true? @should-run)
      (let [^ConsumerRecords records (.poll consumer 100)]
        (doseq [^ConsumerRecord record records]
          (-> (mg.coll/insert-and-return db "messages" {:value (.value record)})
              (prn)))
        (recur)))))

(defn stop [consumer mongo]
  (swap! should-run not)
  (.close consumer)
  (close-conn mongo))

(defn -main []
  (prn "Cicin Command started")
  (let [consumer (consume ["messages"])
        mongo (open-conn)]
    (play consumer mongo)))