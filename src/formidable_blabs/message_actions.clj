(ns formidable-blabs.message-actions
  (:require [clojure.core.async :as async :refer [go >!]]
            [clojure.core.match :as match :refer [match]]
            [clojure.core.match.regex :refer :all]
            [formidable-blabs.channels :refer [outbound-channel]]
            [taoensso.timbre :as log]))

;; ### Message Actions
;; Actions based on either the sender of a message, the channel of a message,
;; text in a message, or all three. Divides broadly into commands, emotes, and
;; reactions.
;;
;; #### Commands:
;; !define
;; !whatis
;; !quote
;;
;; #### Emotes:
;; !wat
;; !welp
;; !nope
;; !tableflip
;;
;; ### Reactions:
;; business
;; !darkglasses
;; Hello / goodbye

(defn tableflip
  [message]
  (log/debug "Got a tableflip! Flipping tabble!")
  (let [flips ["(ﾉಥ益ಥ）ﾉ﻿ ┻━┻"
               "┻━┻ ︵ヽ(`Д´)ﾉ︵﻿ ┻━┻"
               "（╯°□°）╯︵ ┻━┻"]
        to-chan (:channel message)]
    (go (>! outbound-channel [to-chan (rand-nth flips)]))))

;; ### Dispatcher
;; **Remember:** Matching is done by `re-matches', which only matches if the _entire
;; string_ matches.

(defn message-dispatch
  ""
  [{:keys [user channel text] :as message}]
  (match [user channel text]
         [_ _ #"(?s)!define.+"] (log/debug "'!wat' command not yet implemented")
         [_ _ #"(?s)!whatis.+"] (log/debug "'!whatis' command not yet implemented")
         [_ _ #"(?s)!quote.+"] (log/debug "'!quote' command not yet implemented")
         [_ _ #"!wat\s*"] (log/debug "'!wat' not yet implemented")
         [_ _ #"!welp\s*"] (log/debug "'!welp' not yet implemented")
         [_ _ #"!nope\s*"] (log/debug "'!nope' not yet implemented")
         [_ _ #"!tableflip\s*"] (tableflip message)
         :else (log/debug "No message action found.")))
