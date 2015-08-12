package org.datanucleus.scala.test.option

import org.datanucleus.scala.test.BaseSpec
import org.datanucleus.scala.test.samples.bidirectional.option_both_sides.Account
import org.datanucleus.scala.test.samples.bidirectional.option_both_sides.User

import javax.jdo.JDOUserException

class OptionBidirectionalSpec extends BaseSpec {

  "Relationship management" - {

    "with one-to-one bidirectional (Option[FCO] <-> Option[FCO])" - {
      "should check that both sides don't have different objects" in {

        val account = new Account("Account")
        val user = new User("User", account)
        val otherUser = new User("Ohter User")

        // Assign the other user to the other end of the relation
        account.user = Some(otherUser)

        val e = intercept[JDOUserException] {
          val id = persist(user) // Should be gone
        }
        
        val expectedMessage = s"""Object "$user"
                             | has field "${user.getClass.getName}.account"
                             | with a 1-1 bidirectional relation set to relate to "$account"
                             | but this object relates back to a different object "$otherUser"!"""
          .stripMargin.replaceAll("\n", "")

        assert(e.getMessage == expectedMessage)
      }

      "should manage the OWNER SIDE of the relation" - {
        "when creating a new object and setting only the mapped side" in {

          val user = new User("User")
          val account = new Account("Account", user)

          val id = persist(account)

          transactional {
            assert(user.account.value == account)
          }
        }

        "when modifiying the mapped side of existing relationship with a new object" in {

          val user = new User("User")
          val account = new Account("Account", user)
          user.account = Some(account)

          val id = persist(account)

          val newUser = new User("NEW USER")

          val (persistedAccount, previousUser) =
            transactional {

              val persistedAccount = pm.getObjectById(id).asInstanceOf[Account]
              val previousUser = persistedAccount.user.get

              persistedAccount.user = Some(newUser)

              (persistedAccount, previousUser)
            }

          transactional {
            // datanucleus.manageRelationships
            assert(newUser.account.value == persistedAccount, "Must set the other side of relation for the current object")
            assert(previousUser.account == None, "Must unset the other side of the old related object")
          }
        }

        "when modifiying the mapped side of existing relationship to a None" in {

          val user = new User("User")
          val account = new Account("Account", user)
          user.account = Some(account)

          val id = persist(account)

          val (persistedAccount, previousUser) =
            transactional {

              val persistedAccount = pm.getObjectById(id).asInstanceOf[Account]
              val previousUser = persistedAccount.user.get

              persistedAccount.user = None

              (persistedAccount, previousUser)
            }

          transactional {
            // datanucleus.manageRelationships
            assert(previousUser.account == None, "Must unset the other side of the old related object")
          }
        }

        "when deleting the mapped side of existing relationship" in {

          val user = new User("User")
          val account = new Account("Account", user)
          user.account = Some(account)

          val id = persist(account)

          val previousUser =
            transactional {

              val persistedAccount = pm.getObjectById(id).asInstanceOf[Account]
              val previousUser = persistedAccount.user.get

              delete(persistedAccount)

              previousUser
            }

          transactional {
            // datanucleus.manageRelationships
            assert(previousUser.account == None, "Must unset the other side of the old related object")
          }
        }
      }

      "should manage the MAPPED side of the relation" - {
        "when creating a new object and setting only the owner side" in {

          val account = new Account("Account")
          val user = new User("User", account)

          val id = persist(user)

          transactional {
            assert(account.user.value === user)
          }
        }

        "when modifiying the owner side of existing relationship with a new object" in {

          val account = new Account("Account")
          val user = new User("User", account)
          account.user = Some(user)

          val id = persist(user)

          val newAccount = new Account("New Account")

          val (persistedUser, previousAccount) =
            transactional {

              val persistedUser = pm.getObjectById(id).asInstanceOf[User]
              val previousAccount = persistedUser.account.get

              persistedUser.account = Some(newAccount)

              (persistedUser, previousAccount)
            }

          transactional {
            // datanucleus.manageRelationships
            assert(newAccount.user.value == persistedUser, "Must set the other side of relation for the current object")
            assert(previousAccount.user == None, "Must unset the other side of the old related object")
          }
        }

        "when modifiying the owner side of existing relationship to a None" in {

          val account = new Account("Account")
          val user = new User("User", account)
          account.user = Some(user)

          val id = persist(user)

          val (persistedUser, previousAccount) =
            transactional {

              val persistedUser = pm.getObjectById(id).asInstanceOf[User]
              val previousAccount = persistedUser.account.get

              persistedUser.account = None

              (persistedUser, previousAccount)
            }

          transactional {
            // datanucleus.manageRelationships
            assert(previousAccount.user == None, "Must unset the other side of the old related object")
          }
        }

        "when deleting the owner side of existing relationship" in {

          val account = new Account("Account")
          val user = new User("User", account)
          account.user = Some(user)

          val id = persist(user)

          val previousAccount =
            transactional {

              val persistedUser = pm.getObjectById(id).asInstanceOf[User]
              val previousAccount = persistedUser.account.get

              delete(persistedUser)

              previousAccount
            }

          transactional {
            // datanucleus.manageRelationships
            assert(previousAccount.user == None, "Must unset the other side of the old related object")
          }
        }
      }
    }
  }
  // TODO: Test Container<Interface> and Container:Interface<Intreface>
  //TODO: Test Option[FCO] <-> Collection : One-to-Many bidirectional and opposite mapping
  //  it should "test Company/Department" in {
  //    val company = new Company(None)
  //    val department = new Departament(company)
  //    
  //    persist(department)
  ////    assertThat(Table("USER")).exists()
  ////                             .hasColumns(Column("USER_ID",Types.BIG"BIGINT"),NOT_NULL)
  ////                             .hasForeingKeys(
  ////                               FK())
  //  }

  //  it should "test2 Company/Department regular" in {
  //    val company = new MyCompany()
  //    val department = new MyDepartment(company)
  //
  //    persist(department)
  //  }
  //
  //  it should "test Bank/Customer" in {
  //    transactional{
  //      val bank = new Bank(new java.util.ArrayList())
  //      val customer = new Customer(Some(bank))
  //      bank.customers.add(customer)
  //      pm.makePersistent(customer)
  //      //persist(customer)
  //    }
  //  }
  //  
  //  it should "test Bank/Customer regular" in {
  //    val bank = new MyBank(new java.util.ArrayList())
  //    val customer = new MyCustomer(bank)
  //    bank.customers.add(customer)
  //
  //    persist(customer)
  //  }
  //
  //  it should "test Person/Pet" in {
  //
  //    val person = new Person(None)
  //    val pet = new Pet(person)
  //    person.pet = Some(pet)
  //
  //    persist(person)
  //  }
  //
  //  it should "test Person/Pet regular" in {
  //    
  //    val person = new MyPerson(null)
  //    val pet = new MyPet(person)
  //    person.pet = pet
  //
  //    persist(person)
  //  }
  //
  //  it should "test regular User/Account" in {
  //    
  //    val account = new MyAccount("Account")
  //    val user = new MyUser("User", account)
  //
  //    val id = persist(user)
  //
  //  }

}