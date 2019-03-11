Feature: Gmail

  Scenario: Sending an email with an attached image
  Given I am logged in
	And I am composing an email
	When I attach an image file
	And I click send
	Then the email will be sent to the recipient
	And the image will be included

  Scenario: Sending an email with multiple attached images
  Given I am logged in
	And I am composing an email
	When I attach an image file
	And I attach another image file
	And I click send
	Then the email will be sent to the recipient
	And more than one image will be included

  Scenario: Sending an email after removing the attached image
  Given I am logged in
	And I am composing an email
	And I have attached an image file
	When I remove the attachment
	And I click send
	Then the email will be sent to the recipient
	And the image will not be included

  Scenario: Discarding the email
  Given I am logged in
	And I am composing an email
	And I have attached an image file
	When I click Discard draft
	Then the email will not be sent to the recipient