package nl.suriani.verifyj.example;

import nl.suriani.verifyj.Specification;
import nl.suriani.verifyj.SpecificationOptions;
import nl.suriani.verifyj.action.Action;
import nl.suriani.verifyj.action.Any;
import nl.suriani.verifyj.action.NonDet;
import nl.suriani.verifyj.invariant.Invariants;

public class ValseSpecification {
    public static void main(String... args) {
        var init = nl.suriani.verifyj.action.Action.<Valse>define(
                "init",
                ignored -> new Valse(NonDet.withinRange(-20, 120),
                        NonDet.withinRange(-20, 120),
                        NonDet.withinRange(-20, 120),
                        NonDet.oneOf(ValseStatus.TAKEN_ABACK, ValseStatus.HYPED, ValseStatus.NIRVANA)));

        var takeInSex = Action.<Valse>define(
                "takeInSex",
                valse -> valse.takeIn(FoodFourThought.SEX, NonDet.withinRange(-15, 10)));

        var takeInDrugs = Action.<Valse>define(
                "takeInDrugs",
                valse -> valse.takeIn(FoodFourThought.DRUGS, NonDet.withinRange(-15, 10)));

        var takeInRockAndRoll = Action.<Valse>define(
                "takeInDrugs",
                valse -> valse.takeIn(FoodFourThought.DRUGS, NonDet.withinRange(-15, 10)));

        var step = new Any<>(
                takeInSex,
                takeInDrugs,
                takeInRockAndRoll
        );

        var valseWillEventuallyReachNirvana = Invariants.<Valse>eventually(
                "Valse will eventually reach Nirvana",
                valse -> valse.status == ValseStatus.NIRVANA
        );

        var valseWillEventuallyTopSexAndRockAndRoll = Invariants.<Valse>eventually(
                "Valse will eventually top sex and rock and roll",
                valse -> valse.sex == 100 && valse.rockAndRoll == 100
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



        new Specification<>(
                init,
                step,
                new SpecificationOptions(100, 10),
                valseWillEventuallyReachNirvana,
                valseWillEventuallyTopSexAndRockAndRoll,
                valseWillNeverExceed100InAnyCategory,
                valseWillNeverGoLowerThan0InAnyCategory
        ).run();

    }


    record Valse(int sex, int drugs, int rockAndRoll, ValseStatus status) {

        Valse {

        }

        public Valse takeIn(FoodFourThought foodFourThought, int amount) {
            return this;
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
