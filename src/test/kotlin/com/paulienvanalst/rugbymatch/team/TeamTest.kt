package com.paulienvanalst.rugbymatch.team

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TeamTest {
    @Nested
    @DisplayName("When a team")
    class ValidatingTeamTest {

        private val onePlayerPerPosition = Position.values().map { Player(it, it.ordinal + 1) } +
                Player(Position.LOCK, 12) +
                Player(Position.FLANKER, 13) +
                Player(Position.CENTER, 14) +
                Player(Position.WING, 15)


        @Test
        fun ` with 14 players has not enough players` () {
            val players = onePlayerPerPosition.drop(1)
            assertThat(Team(players, TeamName.RC_TOULON).hasEnoughPlayers, `is`(false))
        }

        @Test
        fun `with 15 players has enough players` () {
            assertThat(Team(onePlayerPerPosition, TeamName.RC_TOULON).hasEnoughPlayers, `is`(true))
        }

        @Test
        fun `with 15 players has no substitute players` () {
            assertThat(Team(onePlayerPerPosition, TeamName.RC_TOULON).hasAnySubstitutes, `is`(false))
        }

        @Test
        fun `with one player per position with back number from 1 to 15 has enough starting players` () {
            assertThat(Team(onePlayerPerPosition, TeamName.RC_TOULON).hasEnoughStartingPlayers, `is`(true))
        }

        @Test
        fun `with one player per position and one extra player has one substitute` () {
            val players = onePlayerPerPosition + Player(Position.LOOSEHEAD_PROP, 17)
            assertThat(Team(players, TeamName.RC_TOULON).hasAnySubstitutes, `is`(true))
        }
    }

    @Nested
    @DisplayName("When a team has a captain")
    class CaptainTest {
        private val onePlayerPerPosition = Position.values().map { Player(it, it.ordinal + 1) } +
                Player(Position.LOCK, 12) +
                Player(Position.FLANKER, 13) +
                Player(Position.CENTER, 14) +
                Player(Position.WING, 15)

        @Test
        fun `it's back number is 9 when a scrumhalf is present`() {
            val team = Team(onePlayerPerPosition, TeamName.RC_TOULON)
            assertThat(team.captainBackNumber(), `is`(7))
        }

        @Test
        fun `it's back number can not be retrieved when a scrumhalf is not present`() {
            val noScrumHalf = onePlayerPerPosition.filter { it.position !=  Position.SCRUM_HALF}
            val team = Team(noScrumHalf, TeamName.RC_TOULON)
            assertThrows(KotlinNullPointerException::class.java, { team.captainBackNumber() })
        }

        @Test
        fun `it's replacing captain back number is something else when a scrumhalf is not present`() {
            val noScrumHalf = onePlayerPerPosition.filter { it.position !=  Position.SCRUM_HALF}
            val team = Team(noScrumHalf, TeamName.RC_TOULON)
            assertThat(team.replacingCaptainBackNumber(), not(7))
        }
    }
}