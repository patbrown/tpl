# tpl

[![bb compatible](https://raw.githubusercontent.com/babashka/babashka/master/logo/badge.svg)](https://babashka.org)
[![Clojars Project](https://img.shields.io/clojars/v/baby.pat/tpl.svg)](https://clojars.org/baby.pat/tpl)

Selmer convenience rapper for BB/CLJ 

___
[<img src="resources/tpl.png" alt="fw" width="400px">](https://tpl.pat.baby)

This selmer wrapper takes paths, paths as strings, just strings, atoms containing strings, maps, atoms containing maps, and files or strings pointing towards maps. The point is to make it as easy to call selmer functions without making a big fuss about how.

## Installation

```clojure
baby.pat/tpl {:mvn/version "0.0.2"}
```
## Usage

```clojure
(require '[baby.pat.tpl :refer [<-tpl <-tpl-keys render]])
(render "Hey {{name}}!" {:name "MOMMA"})
(render "Hey {{name}}!" "resources/my-fav-lady.edn")
(render "resources/my-template.tpl" "resources/my-fav-lady.edn")
(def a-tpl (atom "Hey {{name}}!"))
(<-tpl a-tpl)
(<-tpl-vars "resources/my-template.tpl")
```
