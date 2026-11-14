# Java Banking Application — Final Integration

## Files
- `HasMenu.java` — menu contract
- `User.java` — serializable abstract login/user model
- `CheckingAccount.java` — checking balance and transactions
- `SavingsAccount.java` — savings transactions and interest
- `Customer.java` — customer login, menus, and account report
- `Admin.java` — default admin login and menu
- `Bank.java` — application, logins, admin operations, persistence
- `CustomerList.java` — serializable typed list
- `Input.java` — shared console input helper (extra utility)

## Compile and run
Open a terminal in this folder:

```bash
javac *.java
java Bank
```

On Windows PowerShell or Command Prompt, the same commands work when a JDK is installed.

### Default accounts (testing only)
| User | PIN | Checking | Savings |
|---|---|---:|---:|
| admin | 0000 | n/a | n/a |
| Alice | 1111 | $1000.00 | $1000.00 |
| Bob | 2222 | $0.00 | $0.00 |
| Cindy | 3333 | $0.00 | $0.00 |

Savings interest rate: **5% per application** (one manual admin action, not an annual schedule). Alice's savings changes from $1000 to $1050 after one application.

The program automatically creates `customers.dat` on first launch. Future runs reload that data. To reset the sample data after changing serializable class definitions, uncomment the two reset lines in `Bank()` for one run, then comment them out again. **Reset overwrites existing customer data.** Back up `customers.dat` first if needed.

## Algorithms and pseudocode

### HasMenu
1. Require `menu()` to return a string describing menu choices.
2. Require `start()` to handle the menu loop.

### User constructor and login
1. Store the username and PIN strings.
2. For interactive login, prompt for username and PIN.
3. Compare both entered strings to stored strings.
4. Return true only when both match.
5. Provide setters/getters for username and PIN; require subclasses to implement `getReport()`.

### CheckingAccount constructor, menu, start, and transactions
1. Initialize a nonnegative account balance.
2. Display checking menu and read a whole-number choice.
3. If choice is 0, return to the previous menu.
4. If choice is 1, print formatted balance.
5. If choice is 2, ask for a positive deposit amount, add it to balance, print result.
6. If choice is 3, ask for a positive withdrawal amount.
7. If withdrawal exceeds balance, report insufficient funds; otherwise subtract and print result.
8. For any other choice, display invalid selection and repeat.
9. `getBalance()` returns the numeric balance; `getBalanceString()` formats currency.
10. `setBalance()` rejects negative and non-finite amounts.

### SavingsAccount constructor, menu, start, and calcInterest
1. Initialize inherited balance and interest rate (default 0.05).
2. Display savings menu and read selection.
3. Delegate balance, deposit, and withdrawal choices to inherited methods.
4. Print interest rate for choice 4; return for choice 0.
5. `calcInterest()`: compute `balance * (1 + interestRate)`; set balance; print updated balance.
6. `setInterestRate()` rejects negative and non-finite rates.

### Customer constructor, menu, start, changePIN, and report
1. Initialize inherited username and PIN.
2. Create a checking account and savings account.
3. Display customer menu and read selection until exit.
4. For checking or savings choices, invoke that account's `start()` method.
5. For report, print `getReport()`.
6. For change PIN, confirm existing PIN, validate new PIN as exactly four digits, then update it.
7. `getReport()` concatenates username and formatted checking/savings balances.

### Admin constructor, menu, start, and report
1. Set admin username to `admin` and PIN to `0000`.
2. Return a menu containing exit, full report, add user, and apply interest.
3. Leave `start()` empty as required; Bank executes the admin actions.
4. Return admin credentials in `getReport()` for this assignment only.

### Bank constructor and main menu
1. Create one Admin and one CustomerList.
2. Load saved customers, or initialize and save sample customers if no file exists.
3. Start the main bank menu.
4. When the menu ends, save customers.
5. Main menu loop: show menu, read option, exit on 0.
6. On 1, authenticate admin; if successful call `startAdmin()`.
7. On 2, call `loginAsCustomer()`.
8. Reject invalid selections.

### Bank.startAdmin / adminStart
1. Print admin's menu and read option in a loop.
2. On 0, return to main menu.
3. On 1, invoke `fullCustomerReport()`.
4. On 2, invoke `addUser()`.
5. On 3, invoke `applyInterest()`.
6. Reject invalid options and repeat.

### Bank.fullCustomerReport
1. Print report heading.
2. Loop through all customers.
3. Print each customer's `getReport()` string.

### Bank.addUser
1. Ask for new username.
2. Reject blank username, reserved `admin`, or duplicate usernames.
3. Ask for a PIN and validate four numeric characters.
4. Construct a Customer and append to the CustomerList.
5. Save the list and confirm creation.

### Bank.applyInterest
1. Print interest heading.
2. Loop through all customers.
3. Call `getSavings().calcInterest()` for each customer.
4. Save updated customer list.

### Bank.loginAsCustomer
1. Prompt for username and PIN once.
2. Set current customer to null.
3. Loop through the CustomerList and test `login(username, PIN)`.
4. If a match is found, store that Customer and stop searching.
5. If no match is found, print login failure.
6. Otherwise invoke the matched Customer's `start()` method.
7. Save changes when the customer returns to the bank menu.

### Bank.loadSampleCustomers
1. Create an empty CustomerList.
2. Create Alice with PIN 1111; set checking and savings balances to $1000 each.
3. Add Alice, Bob (PIN 2222), and Cindy (PIN 3333).

### Bank.saveCustomers
1. Open a temporary file for object output.
2. Serialize the entire CustomerList object.
3. Close the output stream.
4. Replace the saved data file with the completed temporary file.
5. If saving fails, print an error.

### Bank.loadCustomers
1. Check whether the saved data file exists.
2. If it does not, load sample customers and save them.
3. Otherwise, open object input stream and read one object.
4. Verify that the object is a CustomerList and assign it to the bank.
5. On failure, explain the error and stop rather than overwrite existing data.

### CustomerList
1. Extend `ArrayList<Customer>` to provide a named serializable collection type.

### Input helper
1. Keep one static Scanner for console input, outside serializable instances.
2. Read and trim input lines.
3. For integers, retry until a valid whole number is entered.
4. For amounts, retry until a positive finite number is entered.

## Test checklist
- [ ] First launch creates sample customers and `customers.dat`.
- [ ] Wrong admin credentials are rejected.
- [ ] `admin` / `0000` displays admin menu.
- [ ] Admin report shows Alice $1000/$1000, Bob $0/$0, Cindy $0/$0.
- [ ] Add Dave with PIN 4444 and confirm he appears in report.
- [ ] Apply interest once and verify Alice's savings becomes $1050.00.
- [ ] Login as Alice / 1111 and make a deposit and withdrawal.
- [ ] Reject a withdrawal larger than available balance.
- [ ] Reject invalid menu choices and duplicate usernames.
- [ ] Exit and restart; verify balances and Dave remain saved.
- [ ] Customer can change PIN after entering current PIN.

## Extra effort (clearly marked for rubric)
- **Input validation:** rejects invalid menu numbers, negative/zero transaction amounts, overdrafts, and duplicate usernames.
- **Customer PIN change:** customer can update PIN after confirming the current one.
- **Safer saving:** writes a temporary serialized file before replacing the saved file.
- **Friendly login messages:** clear success/failure prompts and customer welcome.

## Notes
This is a learning project, not a secure production bank. Passwords/PINs are stored as plaintext in serialized data and admin report can expose the admin PIN; do not use real credentials or real customer data. Money uses `double` to match the supplied UML, though real financial software should use `BigDecimal` or integer cents.
