package nl.suriani.verifyj.example.rockingjack;

import nl.suriani.verifyj.*;

class RockingJackSpecification {
    public static void main(String... args) {
        var init = new Init<>(
                () -> RockingJack.init(NonDet.withinRange(-20, 120),
                        NonDet.withinRange(-20, 120),
                        NonDet.withinRange(-20, 120)));

        var takeInSex = new NamedAction<RockingJack>(
                "takeInSex",
                jack -> jack.takeIn(FoodFourThought.SEX, NonDet.withinRange(-15, 35)));

        var takeInDrugs = new NamedAction<RockingJack>(
                "takeInDrugs",
                jack -> jack.takeIn(FoodFourThought.DRUGS, NonDet.withinRange(-15, 35)),
                jack -> jack.status() != RockingJackStatus.NIRVANA);

        var takeInRockAndRoll = new NamedAction<RockingJack>(
                "takeInRockAndRoll",
                jack -> jack.takeIn(FoodFourThought.ROCK_AND_ROLL, NonDet.withinRange(-15, 35)));

        var step = new Step<>(
                takeInSex,
                takeInDrugs,
                takeInRockAndRoll
        );

        var jackWillEventuallyReachNirvana = TemporalProperties.<RockingJack>atLast(
                "RockingJack will at last reach Nirvana",
                jack -> jack.status() == RockingJackStatus.NIRVANA
        );

        var jackWillAtLastTopSexAndRockAndRoll = TemporalProperties.<RockingJack>atLast(
                "RockingJack will at last top sex and rock and roll",
                jack -> jack.sex() == 100 && jack.rockAndRoll() == 100
        );

        var jackWillEventuallyTopDrugs = TemporalProperties.<RockingJack>eventually(
                "RockingJack will eventually top drugs",
                jack -> jack.drugs() == 100
        );

        var jackWillEventuallyBeDrugsFree = TemporalProperties.<RockingJack>eventually(
                "RockingJack will eventually be drugs free",
                jack -> jack.drugs() == 0
        );

        var jackWillNeverExceed100InAnyCategory = TemporalProperties.<RockingJack>never(
                "RockingJack will never exceed 100 in any category",
                jack -> jack.sex() > 100 ||
                        jack.drugs() > 100 ||
                        jack.rockAndRoll() > 100);

        var jackWillNeverGoLowerThan0InAnyCategory = TemporalProperties.<RockingJack>never(
                "RockingJack will never go lower than 0 in any category",
                jack -> jack.sex() < 0 ||
                        jack.drugs() < 0 ||
                        jack.rockAndRoll() < 0);

        var whenSumIsLessThan50RockingJackIsTakenAbackUnlessSexIs60OrMoreOrHeFoundNirvana = TemporalProperties.<RockingJack>always(
                "When sum is less than 100, RockingJack is taken aback unless sex is 60 or more or he found Nirvana",
                jack ->  {
                    if ((jack.sex() + jack.drugs() + jack.rockAndRoll() < 100) &&
                            (jack.sex() < 60 && jack.status() != RockingJackStatus.NIRVANA)) {

                        return jack.status() == RockingJackStatus.TAKEN_ABACK;
                    }
                    return true;
                }
        );


        var specification = new Specification<>(init, step)
                .withTemporalProperties(
                    jackWillEventuallyReachNirvana,
                    jackWillAtLastTopSexAndRockAndRoll,
                    jackWillEventuallyTopDrugs,
                    jackWillEventuallyBeDrugsFree,
                    jackWillNeverExceed100InAnyCategory,
                    jackWillNeverGoLowerThan0InAnyCategory,
                    whenSumIsLessThan50RockingJackIsTakenAbackUnlessSexIs60OrMoreOrHeFoundNirvana
                );

        var runner = new Simulator<RockingJack>(
                new SimulationOptions(50, 500, 250, true)
        );

        var report = runner.run(specification);
        System.out.println(report);

        System.out.println(new StateSpaceDigraphGenerator<RockingJack>(
                jack -> getStatus(jack)

        ).run(report));
    }

    private static String getStatus(RockingJack jack) {
        var sum = jack.sex() + jack.drugs() + jack.rockAndRoll();
        if (jack.status() == RockingJackStatus.NIRVANA) {
            return "Jack_NIRVANA";
        }

        if (sum >= 250) {
            return "Jack_Approaching_Nirvana";
        }

        if (sum >= 200) {
            return "Jack_Hyped";
        }

        if (sum >= 150) {
            return "Jack_Mildly_Taken_Aback";
        }

        if (sum >= 100) {
            return "Jack_Taken_Aback";
        }

        if (sum >= 50) {
            return "Jack_Mildly_Depressed";
        }

        return "Jack_Depressed";

    }


    enum RockingJackStatus {
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
