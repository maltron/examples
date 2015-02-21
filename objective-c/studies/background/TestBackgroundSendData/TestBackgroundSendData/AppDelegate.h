//
//  AppDelegate.h
//  TestBackgroundSendData
//
//  Created by Mauricio Leal on 2/7/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

#define SERVER @"http://localhost:8080/person/rest/resource"

@class Person;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

@property (readonly, nonatomic, strong) NSURLSessionConfiguration *urlSessionConfiguration;
@property (readonly, nonatomic, strong) NSURLSession *urlSession;


+(AppDelegate *)instance;
-(UIAlertView *)alertTitle:(NSString *)title withMessage:(NSString *)message;
-(void)alertAsyncTitle:(NSString *)title withMessage:(NSString *)message;
-(void)alertAsyncNetworkProblem;
-(void)alertAsyncServerProblem;
-(void)alertAsyncJSONProblem;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;
-(NSEntityDescription *)entityPerson;
-(Person *)newPerson;

-(NSURL *)url;
-(NSURL *)urlFindFirst:(NSString *)firstName andLastName:(NSString *)lastName;
-(NSURL *)urlDelete:(Person *)person;

-(void)sendToServer;


@end

