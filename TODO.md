# TODO


## Add multiple simulations per run

* Add nOfSimulations to options
* Add support for multiple simulations in the default runtime

## Save traces

* Add Trace support to the runtime
* Add Trace to Outcome object

## Replay trace support

* Add trace replay support

## Advanced temporal clauses

* Replace implementation of existing temporal clauses (enum) with an ADT (sealed interface + records)
* Add new temporal clauses (as temporal clauses that need a trigger to be evaluated, or depend by adjacent states)

## Add support for constraints

* Add support for constraints (skip evaluation if certain criteria are met) in the runtime

## Advanced logging

* Decouple logging from the runtime
* Add support for custom loggers

## Add support for multiple runtimes

* Decouple further the specification from the runtime
* Facilitate the construction of custom runtimes


## Examples

* Add examples of mutable and anemic models
* Add example of CPU simulation
* Make a specification of the specification itself
* Anonymise ValseSpecification

## Dev UX

* Add builders to simplify the construction of specifications
* Enforce unique name/description for actions and constraints