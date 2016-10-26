CREATE TABLE [CartLines]
(
	[CartLineID] INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
	[UserID] nvarchar(128) NOT NULL,
	[ProductID] INT NOT NULL,
	[Quantity] INT NOT NULL
)