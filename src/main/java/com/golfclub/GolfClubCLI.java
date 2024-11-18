package com.golfclub;

import com.golfclub.model.Member;
import com.golfclub.model.Tournament;
import com.golfclub.service.MemberService;
import com.golfclub.service.TournamentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class GolfClubCLI implements CommandLineRunner {
    private final MemberService memberService;
    private final TournamentService tournamentService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public GolfClubCLI(MemberService memberService, TournamentService tournamentService) {
        this.memberService = memberService;
        this.tournamentService = tournamentService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) {
        System.out.println("Welcome to Golf Club Management System!");
        boolean running = true;
        while (running) {
            displayMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addNewMember();
                    case 2 -> createTournament();
                    case 3 -> registerMemberForTournament();
                    case 4 -> viewMemberStats();
                    case 5 -> viewTournamentStats();
                    case 6 -> searchMembers();
                    case 7 -> searchTournaments();
                    case 8 -> manageMemberships();
                    case 9 -> manageTournamentStatus();
                    case 10 -> generateReports();
                    case 11 -> {
                        System.out.println("Exiting system. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nGolf Club Management System");
        System.out.println("===========================");
        System.out.println("1. Add New Member");
        System.out.println("2. Create Tournament");
        System.out.println("3. Register Member for Tournament");
        System.out.println("4. View Member Statistics");
        System.out.println("5. View Tournament Statistics");
        System.out.println("6. Search Members");
        System.out.println("7. Search Tournaments");
        System.out.println("8. Manage Memberships");
        System.out.println("9. Manage Tournament Status");
        System.out.println("10. Generate Reports");
        System.out.println("11. Exit");
        System.out.print("\nChoose an option (1-11): ");
    }

    private void addNewMember() {
        try {
            System.out.println("\n=== Add New Member ===");
            Member member = new Member();

            System.out.print("Enter member name: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            member.setMemberName(name);

            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            member.setMemberEmail(email);

            System.out.print("Enter phone (XXX-XXX-XXXX): ");
            String phone = scanner.nextLine();
            if (!phone.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
                throw new IllegalArgumentException("Invalid phone format");
            }
            member.setMemberPhone(phone);

            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            if (address.trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be empty");
            }
            member.setMemberAddress(address);

            System.out.print("Enter membership duration (months, 1-60): ");
            int duration = Integer.parseInt(scanner.nextLine());
            if (duration < 1 || duration > 60) {
                throw new IllegalArgumentException("Duration must be between 1 and 60 months");
            }
            member.setDuration(duration);

            member.setStartDate(LocalDate.now());
            member.setStatus(Member.MembershipStatus.ACTIVE);

            memberService.saveMember(member);
            System.out.println("\nMember added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }

    private void createTournament() {
        try {
            System.out.println("\n=== Create New Tournament ===");
            Tournament tournament = new Tournament();

            System.out.print("Enter tournament location: ");
            String location = scanner.nextLine();
            if (location.trim().isEmpty()) {
                throw new IllegalArgumentException("Location cannot be empty");
            }
            tournament.setLocation(location);

            System.out.print("Enter start date (YYYY-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            if (startDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Start date cannot be in the past");
            }
            tournament.setStartDate(startDate);

            System.out.print("Enter end date (YYYY-MM-DD): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }
            tournament.setEndDate(endDate);

            System.out.print("Enter entry fee: ");
            double entryFee = Double.parseDouble(scanner.nextLine());
            if (entryFee < 0) {
                throw new IllegalArgumentException("Entry fee cannot be negative");
            }
            tournament.setEntryFee(entryFee);

            System.out.print("Enter prize amount: ");
            double prizeAmount = Double.parseDouble(scanner.nextLine());
            if (prizeAmount < 0) {
                throw new IllegalArgumentException("Prize amount cannot be negative");
            }
            tournament.setCashPrizeAmount(prizeAmount);

            System.out.print("Enter minimum participants (2 or more): ");
            int minParticipants = Integer.parseInt(scanner.nextLine());
            if (minParticipants < 2) {
                throw new IllegalArgumentException("Minimum participants must be at least 2");
            }
            tournament.setMinimumParticipants(minParticipants);

            System.out.print("Enter maximum participants: ");
            int maxParticipants = Integer.parseInt(scanner.nextLine());
            if (maxParticipants < minParticipants) {
                throw new IllegalArgumentException("Maximum participants must be greater than minimum");
            }
            tournament.setMaximumParticipants(maxParticipants);

            tournament.setStatus(Tournament.TournamentStatus.SCHEDULED);
            tournamentService.saveTournament(tournament);
            System.out.println("\nTournament created successfully!");

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD");
        } catch (Exception e) {
            System.out.println("Error creating tournament: " + e.getMessage());
        }
    }

    private void registerMemberForTournament() {
        try {
            System.out.println("\n=== Register Member for Tournament ===");

            System.out.println("\nAvailable Members:");
            List<Member> members = memberService.getAllMembers();
            for (Member member : members) {
                System.out.printf("%d - %s (%s)%n", member.getId(), member.getMemberName(), member.getStatus());
            }

            System.out.print("\nEnter member ID: ");
            Long memberId = Long.parseLong(scanner.nextLine());

            System.out.println("\nAvailable Tournaments:");
            List<Tournament> tournaments = tournamentService.getAllTournaments();
            for (Tournament tournament : tournaments) {
                System.out.printf("%d - %s at %s (%d/%d participants)%n",
                        tournament.getId(),
                        tournament.getLocation(),
                        tournament.getStartDate(),
                        tournament.getParticipatingMembers().size(),
                        tournament.getMaximumParticipants());
            }

            System.out.print("\nEnter tournament ID: ");
            Long tournamentId = Long.parseLong(scanner.nextLine());

            Tournament updated = tournamentService.addMemberToTournament(tournamentId, memberId);
            System.out.println("\nMember successfully registered for tournament!");

        } catch (Exception e) {
            System.out.println("Error registering member: " + e.getMessage());
        }
    }

    private void viewMemberStats() {
        try {
            System.out.println("\n=== View Member Statistics ===");

            System.out.println("\nAvailable Members:");
            List<Member> members = memberService.getAllMembers();
            for (Member member : members) {
                System.out.printf("%d - %s%n", member.getId(), member.getMemberName());
            }

            System.out.print("\nEnter member ID: ");
            Long memberId = Long.parseLong(scanner.nextLine());

            memberService.getMemberById(memberId).ifPresentOrElse(
                    member -> {
                        System.out.println("\nMember Statistics:");
                        System.out.println("Name: " + member.getMemberName());
                        System.out.println("Email: " + member.getMemberEmail());
                        System.out.println("Phone: " + member.getMemberPhone());
                        System.out.println("Status: " + member.getStatus());
                        System.out.println("Membership Start: " + member.getStartDate());
                        System.out.println("Duration: " + member.getDuration() + " months");
                        System.out.println("Total Tournaments: " + member.getTotalTournamentsPlayed());
                        System.out.println("Total Winnings: $" + member.getTotalWinnings());

                        if (!member.getTournaments().isEmpty()) {
                            System.out.println("\nParticipating Tournaments:");
                            member.getTournaments().forEach(t ->
                                    System.out.printf("- %s at %s (%s)%n",
                                            t.getLocation(), t.getStartDate(), t.getStatus()));
                        }
                    },
                    () -> System.out.println("Member not found")
            );
        } catch (Exception e) {
            System.out.println("Error viewing member stats: " + e.getMessage());
        }
    }

    private void viewTournamentStats() {
        try {
            System.out.println("\n=== View Tournament Statistics ===");

            System.out.println("\nAvailable Tournaments:");
            List<Tournament> tournaments = tournamentService.getAllTournaments();
            for (Tournament t : tournaments) {
                System.out.printf("%d - %s (%s)%n", t.getId(), t.getLocation(), t.getStatus());
            }

            System.out.print("\nEnter tournament ID: ");
            Long tournamentId = Long.parseLong(scanner.nextLine());

            tournamentService.getTournamentById(tournamentId).ifPresentOrElse(
                    tournament -> {
                        System.out.println("\nTournament Statistics:");
                        System.out.println("Location: " + tournament.getLocation());
                        System.out.println("Status: " + tournament.getStatus());
                        System.out.println("Start Date: " + tournament.getStartDate());
                        System.out.println("End Date: " + tournament.getEndDate());
                        System.out.println("Entry Fee: $" + tournament.getEntryFee());
                        System.out.println("Prize Pool: $" + tournament.getCashPrizeAmount());
                        System.out.printf("Participants: %d/%d%n",
                                tournament.getParticipatingMembers().size(),
                                tournament.getMaximumParticipants());

                        if (!tournament.getParticipatingMembers().isEmpty()) {
                            System.out.println("\nRegistered Members:");
                            tournament.getParticipatingMembers().forEach(m ->
                                    System.out.printf("- %s (%s)%n", m.getMemberName(), m.getStatus()));
                        }

                        double totalRevenue = tournament.getEntryFee() * tournament.getParticipatingMembers().size();
                        System.out.printf("\nTotal Revenue: $%.2f%n", totalRevenue);
                    },
                    () -> System.out.println("Tournament not found")
            );
        } catch (Exception e) {
            System.out.println("Error viewing tournament stats: " + e.getMessage());
        }
    }

    private void searchMembers() {
        try {
            System.out.println("\n=== Search Members ===");
            System.out.println("1. Search by Name");
            System.out.println("2. Search by Phone");
            System.out.println("3. Search by Status");
            System.out.println("4. Search by Tournament Participation");
            System.out.print("\nChoose search option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            List<Member> results = switch (choice) {
                case 1 -> {
                    System.out.print("Enter name to search: ");
                    yield memberService.searchByName(scanner.nextLine());
                }
                case 2 -> {
                    System.out.print("Enter phone number: ");
                    yield memberService.searchByPhone(scanner.nextLine());
                }
                case 3 -> {
                    System.out.println("Enter status (ACTIVE/EXPIRED/SUSPENDED/PENDING): ");
                    yield memberService.findByStatus(Member.MembershipStatus.valueOf(scanner.nextLine()));
                }
                case 4 -> {
                    System.out.print("Enter minimum number of tournaments: ");
                    yield memberService.findByMinimumTournaments(Integer.parseInt(scanner.nextLine()));
                }
                default -> {
                    System.out.println("Invalid option");
                    yield List.of();
                }
            };

            displayMemberResults(results);
        } catch (Exception e) {
            System.out.println("Error searching members: " + e.getMessage());
        }
    }

    private void searchTournaments() {
        try {
            System.out.println("\n=== Search Tournaments ===");
            System.out.println("1. Search by Location");
            System.out.println("2. Search by Date Range");
            System.out.println("3. Search by Status");
            System.out.print("\nChoose search option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            List<Tournament> results = switch (choice) {
                case 1 -> {
                    System.out.print("Enter location: ");
                    yield tournamentService.findByLocation(scanner.nextLine());
                }
                case 2 -> {
                    try {
                        System.out.print("Enter start date (YYYY-MM-DD): ");
                        LocalDate start = LocalDate.parse(scanner.nextLine(), dateFormatter);
                        System.out.print("Enter end date (YYYY-MM-DD): ");
                        LocalDate end = LocalDate.parse(scanner.nextLine(), dateFormatter);
                        yield tournamentService.findByDateRange(start, end);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format");
                        yield List.of();
                    }
                }
                case 3 -> {
                    System.out.println("Enter status (SCHEDULED/IN_PROGRESS/COMPLETED/CANCELLED): ");
                    yield tournamentService.findByStatus(Tournament.TournamentStatus.valueOf(scanner.nextLine().toUpperCase()));
                }
                default -> {
                    System.out.println("Invalid option");
                    yield List.of();
                }
            };

            displayTournamentResults(results);
        } catch (Exception e) {
            System.out.println("Error searching tournaments: " + e.getMessage());
        }
    }

    private void manageMemberships() {
        try {
            System.out.println("\n=== Manage Memberships ===");

            System.out.println("\nCurrent Members:");
            List<Member> members = memberService.getAllMembers();
            for (Member member : members) {
                System.out.printf("%d - %s (Current Status: %s)%n",
                        member.getId(), member.getMemberName(), member.getStatus());
            }

            System.out.print("\nEnter member ID to manage: ");
            Long memberId = Long.parseLong(scanner.nextLine());

            System.out.println("\nSelect new status:");
            System.out.println("1. ACTIVE");
            System.out.println("2. SUSPENDED");
            System.out.println("3. EXPIRED");
            System.out.println("4. PENDING");

            int choice = Integer.parseInt(scanner.nextLine());
            Member.MembershipStatus newStatus = switch (choice) {
                case 1 -> Member.MembershipStatus.ACTIVE;
                case 2 -> Member.MembershipStatus.SUSPENDED;
                case 3 -> Member.MembershipStatus.EXPIRED;
                case 4 -> Member.MembershipStatus.PENDING;
                default -> throw new IllegalArgumentException("Invalid status choice");
            };

            memberService.updateMemberStatus(memberId, newStatus);
            System.out.println("\nMembership status updated successfully!");

        } catch (Exception e) {
            System.out.println("Error managing membership: " + e.getMessage());
        }
    }

    private void manageTournamentStatus() {
        try {
            System.out.println("\n=== Manage Tournament Status ===");

            System.out.println("\nCurrent Tournaments:");
            List<Tournament> tournaments = tournamentService.getAllTournaments();
            for (Tournament tournament : tournaments) {
                System.out.printf("%d - %s (Current Status: %s)%n",
                        tournament.getId(), tournament.getLocation(), tournament.getStatus());
            }

            System.out.print("\nEnter tournament ID to manage: ");
            Long tournamentId = Long.parseLong(scanner.nextLine());

            System.out.println("\nSelect new status:");
            System.out.println("1. SCHEDULED");
            System.out.println("2. IN_PROGRESS");
            System.out.println("3. COMPLETED");
            System.out.println("4. CANCELLED");

            int choice = Integer.parseInt(scanner.nextLine());
            Tournament.TournamentStatus newStatus = switch (choice) {
                case 1 -> Tournament.TournamentStatus.SCHEDULED;
                case 2 -> Tournament.TournamentStatus.IN_PROGRESS;
                case 3 -> Tournament.TournamentStatus.COMPLETED;
                case 4 -> Tournament.TournamentStatus.CANCELLED;
                default -> throw new IllegalArgumentException("Invalid status choice");
            };

            tournamentService.updateTournamentStatus(tournamentId, newStatus);
            System.out.println("\nTournament status updated successfully!");

        } catch (Exception e) {
            System.out.println("Error managing tournament status: " + e.getMessage());
        }
    }

    private void generateReports() {
        try {
            System.out.println("\n=== Generate Reports ===");
            System.out.println("1. Active Members Report");
            System.out.println("2. Tournament Revenue Report");
            System.out.println("3. Member Participation Report");
            System.out.print("\nSelect report type: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> generateActiveMembersReport();
                case 2 -> generateTournamentRevenueReport();
                case 3 -> generateMemberParticipationReport();
                default -> System.out.println("Invalid report option");
            }
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    private void displayMemberResults(List<Member> members) {
        if (members.isEmpty()) {
            System.out.println("\nNo members found");
            return;
        }

        System.out.println("\nMembers found:");
        for (Member member : members) {
            System.out.printf("ID: %d | Name: %s | Status: %s | Email: %s | Phone: %s%n",
                    member.getId(),
                    member.getMemberName(),
                    member.getStatus(),
                    member.getMemberEmail(),
                    member.getMemberPhone());
        }
    }

    private void displayTournamentResults(List<Tournament> tournaments) {
        if (tournaments.isEmpty()) {
            System.out.println("\nNo tournaments found");
            return;
        }

        System.out.println("\nTournaments found:");
        for (Tournament tournament : tournaments) {
            System.out.printf("ID: %d | Location: %s | Status: %s | Date: %s | Participants: %d/%d%n",
                    tournament.getId(),
                    tournament.getLocation(),
                    tournament.getStatus(),
                    tournament.getStartDate(),
                    tournament.getParticipatingMembers().size(),
                    tournament.getMaximumParticipants());
        }
    }

    private void generateActiveMembersReport() {
        List<Member> activeMembers = memberService.findByStatus(Member.MembershipStatus.ACTIVE);
        System.out.println("\n=== Active Members Report ===");
        System.out.println("Total Active Members: " + activeMembers.size());
        displayMemberResults(activeMembers);
    }

    private void generateTournamentRevenueReport() {
        Double totalRevenue = tournamentService.calculateTotalRevenue();
        System.out.println("\n=== Tournament Revenue Report ===");
        System.out.printf("Total Revenue Across All Tournaments: $%.2f%n", totalRevenue);

        List<Tournament> tournaments = tournamentService.getAllTournaments();
        System.out.println("\nRevenue by Tournament:");
        for (Tournament tournament : tournaments) {
            double tournamentRevenue = tournament.getEntryFee() * tournament.getParticipatingMembers().size();
            System.out.printf("- %s: $%.2f (Participants: %d, Entry Fee: $%.2f)%n",
                    tournament.getLocation(),
                    tournamentRevenue,
                    tournament.getParticipatingMembers().size(),
                    tournament.getEntryFee());
        }
    }

    private void generateMemberParticipationReport() {
        System.out.println("\n=== Member Participation Report ===");
        System.out.print("Enter minimum tournament count: ");
        try {
            int minCount = Integer.parseInt(scanner.nextLine());
            List<Member> activeParticipants = memberService.findByMinimumTournaments(minCount);

            System.out.printf("\nMembers with %d or more tournaments:%n", minCount);
            for (Member member : activeParticipants) {
                System.out.printf("- %s: %d tournaments%n",
                        member.getMemberName(),
                        member.getTotalTournamentsPlayed());
            }

            if (!activeParticipants.isEmpty()) {
                double avgTournaments = activeParticipants.stream()
                        .mapToInt(Member::getTotalTournamentsPlayed)
                        .average()
                        .orElse(0.0);
                System.out.printf("\nAverage tournaments per member: %.1f%n", avgTournaments);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number");
        }
    }
}