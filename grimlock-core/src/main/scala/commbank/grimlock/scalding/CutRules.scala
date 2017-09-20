// Copyright 2014,2015,2016,2017 Commonwealth Bank of Australia
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package commbank.grimlock.scalding.transform

import commbank.grimlock.framework.position.Position

import commbank.grimlock.scalding.environment.Context

import commbank.grimlock.library.transform.{ CutRules => FwCutRules }

import com.twitter.scalding.typed.LiteralValue

import shapeless.HList

/** Implement cut rules using Scalding. */
object CutRules extends FwCutRules[Context.E] {
  def fixed[
    K <: HList,
    V <: HList
  ](
    ext: Context.E[Stats[K, V]],
    min: Position[V],
    max: Position[V],
    k: Long
  ): Context.E[Map[Position[K], List[Double]]] = ext.map { case stats => fixedFromStats(stats, min, max, k) }

  def squareRootChoice[
    K <: HList,
    V <: HList
  ](
    ext: Context.E[Stats[K, V]],
    count: Position[V],
    min: Position[V],
    max: Position[V]
  ): Context.E[Map[Position[K], List[Double]]] = ext
    .map { case stats => squareRootChoiceFromStats(stats, count, min, max) }

  def sturgesFormula[
    K <: HList,
    V <: HList
  ](
    ext: Context.E[Stats[K, V]],
    count: Position[V],
    min: Position[V],
    max: Position[V]
  ): Context.E[Map[Position[K], List[Double]]] = ext
    .map { case stats => sturgesFormulaFromStats(stats, count, min, max) }

  def riceRule[
    K <: HList,
    V <: HList
  ](
    ext: Context.E[Stats[K, V]],
    count: Position[V],
    min: Position[V],
    max: Position[V]
  ): Context.E[Map[Position[K], List[Double]]] = ext.map { case stats => riceRuleFromStats(stats, count, min, max) }

  def doanesFormula[
    K <: HList,
    V <: HList
  ](
    ext: Context.E[Stats[K, V]],
    count: Position[V],
    min: Position[V],
    max: Position[V],
    skewness: Position[V]
  ): Context.E[Map[Position[K], List[Double]]] = ext
    .map { case stats => doanesFormulaFromStats(stats, count, min, max, skewness) }

  def scottsNormalReferenceRule[
    K <: HList,
    V <: HList
  ](
    ext: Context.E[Stats[K, V]],
    count: Position[V],
    min: Position[V],
    max: Position[V],
    sd: Position[V]
  ): Context.E[Map[Position[K], List[Double]]] = ext
    .map { case stats => scottsNormalReferenceRuleFromStats(stats, count, min, max, sd) }

  def breaks[
    K <: HList
  ](
    range: Map[Position[K], List[Double]]
  ): Context.E[Map[Position[K], List[Double]]] = new LiteralValue(range)
}

