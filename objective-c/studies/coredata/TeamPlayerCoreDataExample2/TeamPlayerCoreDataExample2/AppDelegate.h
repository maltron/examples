//
//  AppDelegate.h
//  TeamPlayerCoreDataExample2
//
//  Created by Mauricio Leal on 1/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "Team.h"
#import "Player.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

+(AppDelegate *)instance;
-(UIAlertView *)alertTitle:(NSString *)title withMessage:(NSString *)message;
-(NSEntityDescription *)entityPlayer;
-(NSEntityDescription *)entityTeam;

-(Team *)newTeam;
-(Player *)newPlayer;
-(BOOL)teamAlreadyExist:(Team *)current withName:(NSString *)name;
-(BOOL)playerAlreadyExist:(Player *)current withName:(NSString *)name within:(Team *)team;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;


@end

