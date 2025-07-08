package nl.suriani.verifyj.example.rockingjack;

record RockingJack(int sex, int drugs, int rockAndRoll, RockingJackSpecification.RockingJackStatus status) {

    RockingJack(int sex, int drugs, int rockAndRoll, RockingJackSpecification.RockingJackStatus status) {
        this.sex = clamp(sex, 0, 100);
        this.drugs = clamp(drugs, 0, 100);
        this.rockAndRoll = clamp(rockAndRoll, 0, 100);
        this.status = updateStatus(sex, drugs, rockAndRoll, status);
    }

    public static RockingJack init(int sex, int drugs, int rockAndRoll) {
        return new RockingJack(sex, drugs, rockAndRoll, RockingJackSpecification.RockingJackStatus.TAKEN_ABACK);
    }

    public RockingJack takeIn(RockingJackSpecification.FoodFourThought foodFourThought, int amount) {

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

        return new RockingJack(newSex, newDrugs, newRockAndRoll, newStatus);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private RockingJackSpecification.RockingJackStatus updateStatus(int drugs, int sex, int rockAndRoll, RockingJackSpecification.RockingJackStatus status) {
        if (status == RockingJackSpecification.RockingJackStatus.NIRVANA) {
            return status;
        }

        if (drugs + sex + rockAndRoll < 100 && sex < 60) {
            return RockingJackSpecification.RockingJackStatus.TAKEN_ABACK;
        }

        if (drugs == 100 && sex == 100 && rockAndRoll == 100) {
            return RockingJackSpecification.RockingJackStatus.NIRVANA;
        }

        return RockingJackSpecification.RockingJackStatus.HYPED;
    }
}
