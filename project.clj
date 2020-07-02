(defproject cicin-command "0.1.0"
  :description "Command consumidor do tópico unificado de transações"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]

                 ;kafka
                 [org.apache.kafka/kafka-clients "1.1.0"]
                 [fundingcircle/jackdaw "0.6.4"]

                 ;json
                 [cheshire "5.9.0"]

                 ;MongoDB
                 [com.novemberain/monger "3.5.0" :exclusions [com.google.guava/guava]]]
  :repl-options {:init-ns cicin-command.core}
  :main ^:skip-aot cicin-command.core)
