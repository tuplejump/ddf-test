/*
 * Licensed to Tuplejump Software Pvt. Ltd. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Tuplejump Software Pvt. Ltd. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except)compliance
 * with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to)writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.ddf

import java.util.Collections

import io.ddf.etl.Types.JoinType
import org.scalatest.{Matchers}

import scala.collection.JavaConversions._

trait JoinSpec extends BaseSpec with Matchers {

  feature("Join") {
    scenario("inner join tables") {
      val ddf: DDF = loadAirlineDDF()
      val ddf2: DDF = loadYearNamesDDF()
      val joinedDDF = ddf.join(ddf2, null, null, Collections.singletonList("Year"), Collections.singletonList("Year_num"))
      val distinctDDF = joinedDDF.sql2ddf("SELECT DISTINCT YEAR FROM " + joinedDDF.getName)
      distinctDDF.getNumRows should be(2) // only 2 values i.e 2008 and 2010 have values in both tables
    }

    //The left semi inner join tables is not supported for ddf-on-jdbc
    ignore("left semi join tables") {
      val ddf: DDF = loadAirlineDDF
      val ddf2: DDF = loadYearNamesDDF()
      val joinedDDF = ddf.join(ddf2, JoinType.LEFTSEMI, null, Collections.singletonList("Year"), Collections.singletonList("Year_num"))
      val distinctDDF = joinedDDF.sql2ddf("SELECT DISTINCT YEAR FROM " + joinedDDF.getName)
      distinctDDF.getNumRows should be(2) // only 2 values i.e 2008 and 2010 have values in both tables
    }

    scenario("left outer join tables") {
      val ddf: DDF = loadAirlineDDF
      val ddf2: DDF = loadYearNamesDDF()
      val joinedDDF = ddf.join(ddf2, JoinType.LEFT, null, Collections.singletonList("Year"), Collections.singletonList("Year_num"))
      val distinctDDF = joinedDDF.sql2ddf("SELECT DISTINCT YEAR FROM " + joinedDDF.getName)
      distinctDDF.getNumRows should be(3) // 3 distinct values in airline years 2008,2009,2010
    }

    scenario("right outer join tables") {
      val ddf: DDF = loadAirlineDDF
      val ddf2: DDF = loadYearNamesDDF()
      val joinedDDF = ddf.join(ddf2, JoinType.RIGHT, null, Collections.singletonList("Year"), Collections.singletonList("Year_num"))
      val distinctDDF = joinedDDF.sql2ddf("SELECT DISTINCT YEAR FROM " + joinedDDF.getName)
      distinctDDF.getNumRows should be(3) // 4 distinct values in airline years 2007,2008,2010,2011
    }

    scenario("full outer join tables") {
      val ddf: DDF = loadAirlineDDF
      val ddf2: DDF = loadYearNamesDDF()
      val joinedDDF = ddf.join(ddf2, JoinType.FULL, null, Collections.singletonList("Year"), Collections.singletonList("Year_num"))
      val distinctDDF = joinedDDF.sql2ddf("SELECT DISTINCT YEAR FROM " + joinedDDF.getName)
      distinctDDF.getNumRows should be(4) //over all 5 distinct years 2007 - 2011 across both tables
    }
  }

}
