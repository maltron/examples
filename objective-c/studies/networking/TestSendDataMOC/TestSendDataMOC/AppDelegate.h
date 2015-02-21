//
//  AppDelegate.h
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/16/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "Person.h"

#define SERVER @"http://localhost:8080/person/rest/resource/"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *mainContext, *backgroundContext;
@property (readonly, strong, strong) NSURLSession *session;

+(AppDelegate *)instance;
-(UIAlertView *)alertTitle:(NSString *)title withMessage:(NSString *)message;
-(void)alertAsyncTitle:(NSString *)title withMessage:(NSString *)message;
-(void)alertAsyncNetworkProblem;
-(void)alertAsyncServerProblem;
-(void)alertAsyncJSONProblem;
-(void)alertAsyncCoreDataProblem;

- (void)saveMainContext;
- (NSURL *)applicationDocumentsDirectory;
-(NSEntityDescription *)entityPerson;
-(Person *)newPerson;

-(NSURL *)url;
-(NSURL *)urlFindFirst:(NSString *)firstName andLastName:(NSString *)lastName;
-(NSURL *)urlDelete:(NSNumber *)iD;

@end

