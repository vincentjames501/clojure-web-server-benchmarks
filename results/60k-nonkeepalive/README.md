# 60k-non-keepalive

> 10k→60k conns (**non**-keepalive).
>
> No comments for this benching profile.

## Results (newest → oldest)

### xfeep.2015.02.17

> No comments from submitter for these results

Detail                  | Value
----------------------- | -----
Submitter               | [@xfeep]
Processor               | 2x 2.10GHz Xeon [E5-2620] v2 (total **24** hardware threads)
Memory                  | 128GB
OS                      | CentOS 7
Clojure                 | 1.7.0-alpha2 on Oracle JDK7
Tool                    | [wrk2] with [patch]
**Text results (raw)**  | [here](20150217-15-00)
Text results (table)    | [here](20150217-15-00-table.txt)

#### xfeep.2015.02.17 - Throughput
![Throughput chart](20150217-15-00-qps.png)

#### xfeep.2015.02.17 - Errors
![Errors chart](20150217-15-00-errs.png)

#### xfeep.2015.02.17 - Mean latency
![Mean latency chart](20150217-15-00-mlat.png)

#### xfeep.2015.02.17 - 99.99% latency
![99.99% latency chart](20150217-15-00-n4lat.png)

[@xfeep]: https://github.com/xfeep
[wrk2]: https://github.com/giltene/wrk2
[patch]: https://github.com/wg/wrk/issues/118#issuecomment-72155351
[E5-2620]: http://ark.intel.com/products/75789/Intel-Xeon-Processor-E5-2620-v2-15M-Cache-2_10-GHz