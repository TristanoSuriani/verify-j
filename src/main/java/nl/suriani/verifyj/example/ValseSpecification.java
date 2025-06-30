package nl.suriani.verifyj.example;

import nl.suriani.verifyj.Specification;
import nl.suriani.verifyj.SpecificationOptions;
import nl.suriani.verifyj.action.Action;
import nl.suriani.verifyj.action.Any;
import nl.suriani.verifyj.action.NonDet;
import nl.suriani.verifyj.invariant.Invariants;

public class ValseSpecification {
    public static void main(String... args) {
        var init = Action.<Valse>define(
                "init",
                ignored -> Valse.init(NonDet.withinRange(-20, 120),
                        NonDet.withinRange(-20, 120),
                        NonDet.withinRange(-20, 120)));

        var takeInSex = Action.<Valse>define(
                "takeInSex",
                valse -> valse.takeIn(FoodFourThought.SEX, NonDet.withinRange(-15, 35)));

        var takeInDrugs = Action.<Valse>define(
                "takeInDrugs",
                valse -> valse.takeIn(FoodFourThought.DRUGS, NonDet.withinRange(-15, 35)));

        var takeInRockAndRoll = Action.<Valse>define(
                "takeInRockAndRoll",
                valse -> valse.takeIn(FoodFourThought.ROCK_AND_ROLL, NonDet.withinRange(-15, 35)));

        var step = new Any<>(
                takeInSex,
                takeInDrugs,
                takeInRockAndRoll
        );

        var valseWillEventuallyReachNirvana = Invariants.<Valse>atLast(
                "Valse will at last reach Nirvana",
                valse -> valse.status == ValseStatus.NIRVANA
        );

        var valseWillAtLastTopSexAndRockAndRoll = Invariants.<Valse>atLast(
                "Valse will at last top sex and rock and roll",
                valse -> valse.sex == 100 && valse.rockAndRoll == 100
        );

        var valseWillEventuallyTopDrugs = Invariants.<Valse>eventually(
                "Valse will eventually top drugs",
                valse -> valse.drugs == 100
        );

        var valseWillEventuallyBeDrugsFree = Invariants.<Valse>eventually(
                "Valse will eventually be drugs free",
                valse -> valse.drugs == 0
        );

        var valseWillNeverExceed100InAnyCategory = Invariants.<Valse>never(
                "Valse will never exceed 100 in any category",
                valse -> valse.sex > 100 ||
                        valse.drugs > 100 ||
                        valse.rockAndRoll > 100);

        var valseWillNeverGoLowerThan0InAnyCategory = Invariants.<Valse>never(
                "Valse will never go lower than 0 in any category",
                valse -> valse.sex < 0 ||
                        valse.drugs < 0 ||
                        valse.rockAndRoll < 0);

        var whenSumIsLessThan50ValseIsTakenAbackUnlessSexIs60OrMoreOrHeFoundNirvana = Invariants.<Valse>always(
                "When sum is less than 100, Valse is taken aback unless sex is 60 or more or he found Nirvana",
                valse ->  {
                    if ((valse.sex + valse.drugs + valse.rockAndRoll < 100) &&
                            (valse.sex < 60 && valse.status != ValseStatus.NIRVANA)) {

                        return valse.status == ValseStatus.TAKEN_ABACK;
                    }
                    return true;
                }
        );


        var specification = new Specification<>(
                init,
                step,
                new SpecificationOptions(500, 250),
                valseWillEventuallyReachNirvana,
                valseWillAtLastTopSexAndRockAndRoll,
                valseWillEventuallyTopDrugs,
                valseWillEventuallyBeDrugsFree,
                valseWillNeverExceed100InAnyCategory,
                valseWillNeverGoLowerThan0InAnyCategory,
                whenSumIsLessThan50ValseIsTakenAbackUnlessSexIs60OrMoreOrHeFoundNirvana
        );

        specification.run();
    }


    record Valse(int sex, int drugs, int rockAndRoll, ValseStatus status) {

        Valse(int sex, int drugs, int rockAndRoll, ValseStatus status) {
            this.sex = clamp(sex, 0, 100);
            this.drugs = clamp(drugs, 0, 100);
            this.rockAndRoll = clamp(rockAndRoll, 0, 100);
            this.status = updateStatus(sex, drugs, rockAndRoll, status);
        }

        public static Valse init(int sex, int drugs, int rockAndRoll) {
            return new Valse(sex, drugs, rockAndRoll, ValseStatus.TAKEN_ABACK);
        }

        public Valse takeIn(FoodFourThought foodFourThought, int amount) {

            amount = clamp(amount, 5, 40);

            var newSex = switch (foodFourThought) {
                case SEX -> clamp(sex + amount, 0, 100);
                default -> sex;
            };

            var newDrugs = switch (foodFourThought) {
                case SEX -> clamp(drugs - 15, 0, 100);
                case DRUGS -> clamp(drugs + amount, 0, 100);
                case ROCK_AND_ROLL -> clamp(drugs - 15, 0, 100);
            };

            var newRockAndRoll = switch (foodFourThought) {
                case ROCK_AND_ROLL -> clamp(rockAndRoll + amount, 0, 100);
                default -> rockAndRoll;
            };

            var newStatus = updateStatus(newDrugs, newSex, newRockAndRoll, this.status);

            return new Valse(newSex, newDrugs, newRockAndRoll, newStatus);
        }

        private int clamp(int value, int min, int max) {
            return Math.max(min, Math.min(max, value));
        }

        private ValseStatus updateStatus(int drugs, int sex, int rockAndRoll, ValseStatus status) {
            if (status == ValseStatus.NIRVANA) {
                return status;
            }

            if (drugs + sex + rockAndRoll < 100 && sex < 60) {
                return ValseStatus.TAKEN_ABACK;
            }

            if (drugs == 100 && sex == 100 && rockAndRoll == 100) {
                return ValseStatus.NIRVANA;
            }

            return ValseStatus.HYPED;
        }
    }

    enum ValseStatus {
        TAKEN_ABACK,
        HYPED,
        NIRVANA
    }

    enum FoodFourThought {
        SEX,
        DRUGS,
        ROCK_AND_ROLL
    }
}
