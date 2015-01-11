//
//  AppDelegate.h
//  TestPredicate
//
//  Created by Mauricio Leal on 1/5/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

#import "Student.h"
#import "School.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

-(Student *)newStudent:(NSString *)name;
-(School *)newSchool:(NSString *)name;
-(School *)fetchSchool:(NSString *)name;
-(void)printSchool:(NSManagedObjectID *)recordID;
-(void)printStudent:(NSManagedObjectID *)recordID;
-(NSArray *)fetchStudentsFromSchool:(NSManagedObjectID *)schoolID;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;


@end

