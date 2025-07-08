package nl.suriani.verifyj.example.rockingjack;

import nl.suriani.verifyj.*;

public class RockingJackSpecification {
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
                jack -> jack.takeIn(FoodFourThought.DRUGS, NonDet.withinRange(-15, 35)));

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
                jack -> jack.to().status() == RockingJackStatus.NIRVANA
        );

        var jackWillAtLastTopSexAndRockAndRoll = TemporalProperties.<RockingJack>atLast(
                "RockingJack will at last top sex and rock and roll",
                jack -> jack.to().sex() == 100 && jack.to().rockAndRoll() == 100
        );

        var jackWillEventuallyTopDrugs = TemporalProperties.<RockingJack>eventually(
                "RockingJack will eventually top drugs",
                jack -> jack.to().drugs() == 100
        );

        var jackWillEventuallyBeDrugsFree = TemporalProperties.<RockingJack>eventually(
                "RockingJack will eventually be drugs free",
                jack -> jack.to().drugs() == 0
        );

        var jackWillNeverExceed100InAnyCategory = TemporalProperties.<RockingJack>never(
                "RockingJack will never exceed 100 in any category",
                jack -> jack.to().sex() > 100 ||
                        jack.to().drugs() > 100 ||
                        jack.to().rockAndRoll() > 100);

        var jackWillNeverGoLowerThan0InAnyCategory = TemporalProperties.<RockingJack>never(
                "RockingJack will never go lower than 0 in any category",
                jack -> jack.to().sex() < 0 ||
                        jack.to().drugs() < 0 ||
                        jack.to().rockAndRoll() < 0);

        var whenSumIsLessThan50RockingJackIsTakenAbackUnlessSexIs60OrMoreOrHeFoundNirvana = TemporalProperties.<RockingJack>always(
                "When sum is less than 100, RockingJack is taken aback unless sex is 60 or more or he found Nirvana",
                jack ->  {
                    if ((jack.to().sex() + jack.to().drugs() + jack.to().rockAndRoll() < 100) &&
                            (jack.to().sex() < 60 && jack.to().status() != RockingJackStatus.NIRVANA)) {

                        return jack.to().status() == RockingJackStatus.TAKEN_ABACK;
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

        var runner = new DefaultRunner<RockingJack>(
                new SimulationOptions(2, 500, 250, true)
        );

        var report = runner.run(specification);
        System.out.println(report);
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
